package pl.aszul.hot.rwb.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.aszul.hot.rwb.model.Attachment;
import pl.aszul.hot.rwb.model.StatusType;
import pl.aszul.hot.rwb.model.UserLocation;
import pl.aszul.hot.rwb.model.WhereIsMode;

import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.removeEnd;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponse {

    private WhereIsMode mode;
    private LocationService locationService;
    private List<UserLocation> networkServiceResponseLocations;
    private boolean detailed;
    private List<Attachment> attachmentList;
    private LocationList locationList;
    private StatusType globalStatus;
    private LocalDateTime globalLastActivity;
    private String responseText;

    public LocationResponse(WhereIsMode mode, LocationService locationService,
                            List<UserLocation> networkServiceResponseLocations, boolean detailed) {
        this.mode = mode;
        this.locationService = locationService;
        this.networkServiceResponseLocations = networkServiceResponseLocations;
        this.detailed = detailed;

        locationList = new LocationList(locationService, networkServiceResponseLocations, detailed);
        this.attachmentList = locationList.getAttachmentList();
        globalStatus = locationList.getGlobalStatus();
        globalLastActivity = locationList.getGlobalLastActivity();
    }

    public void addStatusAttachment(String id, String fullname) {
        attachmentList.add(0, Attachment.statusAttachement(mode, id, fullname, globalStatus, globalLastActivity));
    }

    public void addText(String id, String fullname) {
        responseText = mode.getTitlePrefix() + id + (mode == WhereIsMode.USER ? " (" + fullname + ")" : "") + " is located at ";

        switch (locationList.getLocationsNames().size()) {
            case 1:
                responseText += locationList.getLocationsNames().toArray()[0];
                break;
            case 2:
                responseText += locationList.getLocationsNames().toArray()[0] + " or " +
                        locationList.getLocationsNames().toArray()[1];
                break;
            default:
                for (var i = 0; i < locationList.getNumberOfUserLocations() - 1; i++) {
                    responseText += locationList.getLocationsNames().toArray()[i] + ", ";
                }
                responseText = removeEnd(responseText, ", ");
                responseText += " or " + locationList.getLocationsNames().toArray()[locationList.getNumberOfUserLocations() - 1];
                break;
        }
    }

}
