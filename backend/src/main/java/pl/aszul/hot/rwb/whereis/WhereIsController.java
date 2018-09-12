package pl.aszul.hot.rwb.whereis;

import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pl.aszul.hot.rwb.location.LocationResponse;
import pl.aszul.hot.rwb.location.LocationService;
import pl.aszul.hot.rwb.model.*;
import pl.aszul.hot.rwb.model.rest.RocketChatRequest;
import pl.aszul.hot.rwb.model.rest.RocketChatResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static pl.aszul.hot.rwb.common.Network.getClientDetails;
import static pl.aszul.hot.rwb.config.AppConfig.NETWORK_SERVICE_URL;
import static pl.aszul.hot.rwb.config.SwaggerApiInfoConfig.Operations.*;

@RestController
@RequestMapping("/api/whereis")
@Api(value = "whereis", description = "Rest API for whereis operations", tags = "Whereis")
@Log4j2
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin
class WhereIsController {

    private final LocationService locationService;

    WhereIsController(LocationService locationService/*WhereIsService service*/) {
        this.locationService = locationService;
    }

    private static String createErrorString(Errors errors) {
        return errors.getAllErrors().stream()
                .map(ObjectError::toString)
                .collect(Collectors.joining(","));
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS),
            @ApiResponse(code = 400, message = BAD_REQUEST),
            @ApiResponse(code = 422, message = VALIDATION_ERROR),
            @ApiResponse(code = 404, message = NOT_FOUND)
    })
    @ApiOperation(value = "Returns location data for specified entity", response = RocketChatResponse.class)
    public ResponseEntity<?> whereis(@ApiParam(value = "Id value for entity (user/IP/MAC) to be localized", required = true)
                                     @RequestBody RocketChatRequest rocketChatRequest,
                                     HttpServletRequest request) {
        var rolesSet = Arrays.asList(rocketChatRequest.getSenderRoles())
                .stream()
                .map(role -> role.toUpperCase().replace("-", "_"))
                .collect(Collectors.toSet());

        var detailed = rolesSet.contains("ADMIN") || rolesSet.contains("WHEREIS_DETAILS") || rolesSet.contains("WHEREIS_ADMIN");

        var mode = rocketChatRequest.getMode();

        var responseAttachments = new ArrayList<Attachment>();
        var response = new RocketChatResponse();
        String id, fullname, ip, mac, responseText;
        switch (mode) {
            case USER: case IP: case MAC:
                id = rocketChatRequest.getId();
                fullname = rocketChatRequest.getFullname();

                var restTemplate = new RestTemplate();
                var networkServiceResponse = restTemplate.getForObject(NETWORK_SERVICE_URL + id, NetworkServiceResponse.class);

                var networkServiceResponseLocations = networkServiceResponse.getLocations();
                var numberOfUserLocations = networkServiceResponseLocations.size();
                var single = numberOfUserLocations == 1;

                Set<String> locationsNames = new HashSet<>();

                var locationResponse = new LocationResponse(mode, locationService, networkServiceResponseLocations, detailed);
                locationResponse.addStatusAttachment(id, fullname);
                locationResponse.addText(id, fullname);

                response = new RocketChatResponse(":ghost:", locationResponse.getResponseText(), locationResponse.getAttachmentList().toArray(new Attachment[0]));
                break;
            case NOT_FOUND:
                id = rocketChatRequest.getId();
                log.warn("WhereIs bot was asked about user " + id + " which does NOT EXIST!!!" +
                        "\nRequest details: " + getClientDetails(request));
                responseText = "OK";
                break;
            case NOT_AUTHORIZED:
                id = rocketChatRequest.getId();
                fullname = rocketChatRequest.getFullname();
                log.warn("WhereIs bot was asked about location of user " + id + " by " + fullname + " but he/she is NOT AUTHORIZED to use whereis bot!!!" +
                        "\nRequest details: " + getClientDetails(request));
                responseText = "OK";
                break;
            case SYNTAX:
                log.warn("WhereIs bot received incorrect command: " + rocketChatRequest.getCommand() + " which it cannot process!!!");
                break;
        }
        return ResponseEntity.ok(response);
    }

}
