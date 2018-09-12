package pl.aszul.hot.rwb.user;

import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.aszul.hot.rwb.common.UploadFileResponse;
import pl.aszul.hot.rwb.model.LoggedUser;
import pl.aszul.hot.rwb.model.Role;
import pl.aszul.hot.rwb.model.db.*;
import pl.aszul.hot.rwb.model.rest.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.aszul.hot.rwb.common.Network.getClientDetails;
import static pl.aszul.hot.rwb.config.SwaggerApiInfoConfig.Operations.*;
import static pl.aszul.hot.rwb.config.SwaggerApiInfoConfig.Operations.BAD_REQUEST;

//TODO zwracanego usera zanullowac active /activated jesli <> admin
//TODO datat actykwacji
//TODO data modyfikacji usera

@RestController
@RequestMapping
@Api(value = "user", description = "Rest API for users operations", tags = "Users")
@Log4j2
class UserController {

    private final UserService service;
    private final TokenService tokenService;
    private final RevokedTokenService revokedTokenService;
    private final AvatarStorageService avatarStorageService;

    UserController(UserService service, TokenService tokenService, RevokedTokenService revokedTokenService,
                   AvatarStorageService avatarStorageService) {
        this.service = service;
        this.tokenService = tokenService;
        this.revokedTokenService = revokedTokenService;
        this.avatarStorageService = avatarStorageService;
    }

    private static String createErrorString(Errors errors) {
        return errors.getAllErrors().stream()
                .map(ObjectError::toString)
                .collect(Collectors.joining(","));
    }

    @PostMapping("/api/users/login")
    @ApiOperation("Log in to api")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = TOKEN_GENERATED),
            @ApiResponse(code = 401, message = BAD_USER_PASS)
    })
    @ResponseStatus(HttpStatus.OK)
    Token login(@ApiParam(value = "User's login credentials", required = true)
                @Valid @RequestBody UserCredentials credentials, HttpServletRequest request) {
//        final var user = service.findOneByEmail(credentials.getUsername().toLowerCase());
        var user = new LoggedUser();
        user.setUsername(credentials.getUsername());
        final var token = tokenService.exchangePasswordForToken(user, credentials.getPassword());

        final Token result = Token.builder()
                .username(user.getUsername())
                //.email(user.getEmail())
                //.name(user.getName())
                .fullname(user.getFullname())
                .roles(Role.stringArrayToSet(user.getRoles()))
                .expirationDateSeconds(tokenService.getExpirationDateSeconds())
//                .expirationDateSeconds(DateUtils.addSeconds(new Date(), expirationTimeInMinutes))
//                .expirationDate(new Date(Instant.now().plusMillis(expirationTimeInMinutes).toEpochMilli())
                .jwt(token)
                .build();

        log.info("Log in user " + getClientDetails(request, user.getUsername()));
        return result;
    }

    @PostMapping("/api/users/logout")
    @ApiOperation("Log out from api")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = LOGOUT),
            @ApiResponse(code = 401, message = REVOKED),
            @ApiResponse(code = 403, message = FORBIDDEN)
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'WHEREIS_ADMIN')")
    public void logout(@ApiIgnore @AuthenticationPrincipal UserContext userContext, HttpServletRequest request) {
        final var expirationDate = userContext.getExpirationDate();
        final var username = userContext.getUsername();

        final var dbRevokedToken = new DbRevokedToken(username, expirationDate);
        revokedTokenService.logout(dbRevokedToken);
        log.info("Log out user " + getClientDetails(request, username));
    }

    @GetMapping("/api/users/whoami")
    @ApiOperation(value = "Show current user")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS),
            @ApiResponse(code = 401, message = UNAUTHORIZED)
    })
    @PreAuthorize("hasAnyAuthority('ADMIN', 'WHEREIS_ADMIN')")
    //@PreAuthorize("isFullyAuthenticated()")
    public UserContext whoAmI(@ApiIgnore @AuthenticationPrincipal UserContext userContext) {
        return userContext;
    }

    @PostMapping("/avatars")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 400, message = BAD_REQUEST),
            @ApiResponse(code = 401, message = UNAUTHORIZED),
            @ApiResponse(code = 403, message = FORBIDDEN),
            @ApiResponse(code = 422, message = VALIDATION_ERROR)
    })
    @ApiOperation(value = "Uploads new avatar file"/*, response = OfferView.class*/)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'WHEREIS_ADMIN')")
    public UploadFileResponse uploadAvatar(@ApiParam(value = "Map file to be uploaded", required = true)
                                           @RequestParam("file") MultipartFile file,
                                           HttpServletRequest request) {
        var fileName = avatarStorageService.storeFile(file);

        var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/avatars/")
                .path(fileName)
                .toUriString();

        var uploadFileResponse = new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());

        log.info("Map file " + fileName +
                " successfully uploaded to backend server at address " + fileDownloadUri +
                "by user " + getClientDetails(request, "username"));

        return uploadFileResponse;
    }

    @GetMapping("/avatars/{username:.+}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS),
            @ApiResponse(code = 400, message = BAD_REQUEST)
    })
    @ApiOperation(value = "Downloads user avatar file", response = void.class)
    public ResponseEntity<Resource> downloadAvatar(@ApiParam(value = "Avatar file to be downloaded", required = true)
                                                   @PathVariable String username, HttpServletRequest request) {
        Resource resource = avatarStorageService.loadFileAsResource(username);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @DeleteMapping("/avatars/{username}")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS),
            @ApiResponse(code = 400, message = BAD_REQUEST),
            @ApiResponse(code = 401, message = UNAUTHORIZED),
            @ApiResponse(code = 403, message = FORBIDDEN),
    })
    @ApiOperation(value = "Delete avatar file"/*, response = OfferView.class*/)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'WHEREIS_ADMIN')")
    public ResponseEntity<?> deleteAvatar(@ApiParam(value = "Map file to be deleted", required = true)
                                          @RequestParam String username,
                                          @ApiIgnore @AuthenticationPrincipal UserContext userContext,
                                          HttpServletRequest request) {

        if (!UserService.avatarAvailable(username)) {
            return new ResponseEntity<>(Map.of("error", "User " + username + " does not exist!"),
                    HttpStatus.NOT_FOUND);
        }

        try {
            Files.deleteIfExists(Paths.get("./avatars/" + username + ".jpg"));
        } catch (IOException e) {
            return new ResponseEntity<>(Map.of("error", "this user does not have avatar!"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Avatar file for user " + username +
                " successfully deleted by user " +
                getClientDetails(request, (userContext != null ? userContext.getUsername() : "")));

        return ResponseEntity.ok("OK");
    }

}
