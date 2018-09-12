package pl.aszul.hot.rwb.location;

import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.aszul.hot.rwb.common.ConvertibleController;
import pl.aszul.hot.rwb.common.UploadFileResponse;
import pl.aszul.hot.rwb.model.db.DbLocation;
import pl.aszul.hot.rwb.model.rest.LocationView;
import pl.aszul.hot.rwb.model.rest.LocationWrite;
import pl.aszul.hot.rwb.model.rest.RocketChatResponse;
import pl.aszul.hot.rwb.model.rest.UserContext;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static pl.aszul.hot.rwb.common.Network.getClientDetails;
import static pl.aszul.hot.rwb.common.Network.getClientIP;
import static pl.aszul.hot.rwb.config.SwaggerApiInfoConfig.Operations.*;

@RestController
@RequestMapping
@Api(value = "location", description = "Rest API for locations operations", tags = "Locations")
@Log4j2
public class LocationController extends ConvertibleController<DbLocation, LocationView, LocationWrite> {

    private final LocationService locationService;
    private final MapStorageService mapStorageService;

    LocationController(LocationService locationService, MapStorageService mapStorageService) {
        super(DbLocation.class, LocationView.class, LocationWrite.class);
        this.locationService = locationService;
        this.mapStorageService = mapStorageService;
    }

    @GetMapping(value = "/api/locations/{index}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS),
            @ApiResponse(code = 400, message = BAD_REQUEST),
            @ApiResponse(code = 403, message = FORBIDDEN),
            @ApiResponse(code = 404, message = NOT_FOUND),
            @ApiResponse(code = 422, message = VALIDATION_ERROR)
    })
    @ApiOperation(value = "Returns details about specified location", response = RocketChatResponse.class)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'WHEREIS_ADMIN')")
    public ResponseEntity<?> get(@ApiParam(value = "Index number for location to be localized", required = true)
                                 @PathVariable String index) {
        return ResponseEntity.ok(locationService.get(index));
    }

    @GetMapping("/api/locations/")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS),
            @ApiResponse(code = 400, message = BAD_REQUEST),
            @ApiResponse(code = 403, message = FORBIDDEN),
            @ApiResponse(code = 422, message = VALIDATION_ERROR)
    })
    @ApiOperation(value = "Returns all existing locations")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'WHEREIS_ADMIN')")
    List<DbLocation> getAll() {
        return locationService.getAll();
    }

    @PutMapping(value = "/api/locations/{index}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = UPDATED),
            @ApiResponse(code = 401, message = UNAUTHORIZED),
            @ApiResponse(code = 403, message = FORBIDDEN),
            @ApiResponse(code = 404, message = NOT_FOUND),
            @ApiResponse(code = 422, message = VALIDATION_ERROR)
    })
    @ApiOperation(value = "Updates existing location", response = DbLocation.class)
    @PreAuthorize("hasAnyAuthority({'ADMIN', 'WHEREIS_ADMIN'})")
    ResponseEntity<?> update(@ApiParam(value = "Index number of location to be updated", required = true) @PathVariable String index,
                             @ApiParam(value = "Updated location data", required = true) @Valid @RequestBody LocationWrite location,
                             Errors errors,/*, @ApiIgnore @AuthenticationPrincipal UserContext userContext*/
                             HttpServletRequest request) {

        final var dbLocation = convertToDbModel(location);
        dbLocation.setIndex(index);
        log.info(String.format("Location %s successfully updated by user %s", index/*, authenticatedUser*/, getClientIP(request)));
        return ResponseEntity.ok(convertToView(locationService.createOrUpdate(dbLocation)));
    }

    @PostMapping("/api/locations/")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 401, message = UNAUTHORIZED),
            @ApiResponse(code = 403, message = FORBIDDEN),
            @ApiResponse(code = 404, message = NOT_FOUND),
            @ApiResponse(code = 422, message = VALIDATION_ERROR)
    })
    @ApiOperation(value = "Add new location", response = LocationView.class)
    @PreAuthorize("hasAnyAuthority({'ADMIN', 'WHEREIS_ADMIN'})")
    ResponseEntity<?> add(@ApiParam(value = "New location details", required = true) @Valid
                          @RequestBody LocationWrite location,
                          Errors errors, @ApiIgnore @AuthenticationPrincipal UserContext userContext,
                          HttpServletRequest request) {

        final var dbLocation = convertToDbModel(location);
        log.info(String.format("Location %s successfully added by user %s (%s)", dbLocation.toString(), userContext.toString()), getClientIP(request));
        return new ResponseEntity<>(convertToView(locationService.createOrUpdate(dbLocation)), HttpStatus.CREATED);
    }

    @PostMapping("/maps")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 400, message = BAD_REQUEST),
            @ApiResponse(code = 401, message = UNAUTHORIZED),
            @ApiResponse(code = 403, message = FORBIDDEN),
            @ApiResponse(code = 422, message = VALIDATION_ERROR)
    })
    @ApiOperation(value = "Uploads new map file"/*, response = OfferView.class*/)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'WHEREIS_ADMIN')")
    public UploadFileResponse uploadMap(@ApiParam(value = "Map file to be uploaded", required = true)
                                        @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        var fileName = mapStorageService.storeFile(file);

        var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/maps/")
                .path(fileName)
                .toUriString();

        var uploadFileResponse = new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());

        log.info("Map file " + fileName +
                " successfully uploaded to backend server at address " + fileDownloadUri +
                "by user " + getClientDetails(request, "username"));

        return uploadFileResponse;
    }

    @GetMapping("/maps/{index:.+}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS),
            @ApiResponse(code = 400, message = BAD_REQUEST)
    })
    @ApiOperation(value = "Download map file"/*, response = OfferView.class*/)
    public ResponseEntity<Resource> downloadMap(@ApiParam(value = "Map file to be downloaded", required = true)
                                                @PathVariable String index, HttpServletRequest request) {
        Resource resource = mapStorageService.loadFileAsResource(index);

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

    @DeleteMapping("/maps/{index}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS),
            @ApiResponse(code = 400, message = BAD_REQUEST),
            @ApiResponse(code = 401, message = UNAUTHORIZED),
            @ApiResponse(code = 403, message = FORBIDDEN),
    })
    @ApiOperation(value = "Delete map file", response = void.class)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'WHEREIS_ADMIN')")
    public ResponseEntity<?> deleteMap(@ApiParam(value = "Map file to be deleted", required = true)
                                       @RequestParam String index,
                                       @ApiIgnore @AuthenticationPrincipal UserContext userContext,
                                       HttpServletRequest request) {
        log.info("Deleting ./" + index);
        log.info("Usercontext.username = " + userContext.getUsername());
        log.info("Usercontext.getAuthorities = " + userContext.getAuthorities());

        if (!locationService.exists(index)) {
            return new ResponseEntity<>(Map.of("error", "Location " + index + " does not exist!"),
                    HttpStatus.NOT_FOUND);
        }

        if (!locationService.mapAvailable(index)) {
            return new ResponseEntity<>(Map.of("error", "this location does not have map file!"),
                    HttpStatus.NOT_FOUND);
        }

        try {
            Files.deleteIfExists(Paths.get("./maps/" + index + ".jpg"));
        } catch (IOException e) {
            //TODO update error message
            return new ResponseEntity<>(Map.of("error", "this location does not have map!"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Map file for location " + index +
                " successfully deleted by user " + getClientDetails(request, userContext.getUsername()));

        return ResponseEntity.ok("OK");
    }

}
