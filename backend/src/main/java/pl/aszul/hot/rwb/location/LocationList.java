package pl.aszul.hot.rwb.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.aszul.hot.rwb.model.Attachment;
import pl.aszul.hot.rwb.model.StatusType;
import pl.aszul.hot.rwb.model.UserLocation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationList {

    private StatusType globalStatus = StatusType.UNKNOWN;
    private LocalDateTime globalLastActivity =
            LocalDateTime.parse("2018-09-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    private List<Attachment> attachmentList = new ArrayList<>();
    private List<UserLocation> networkServiceResponseLocations;
    private boolean detailed;
    private boolean single;
    private Set<String> locationsNames = new HashSet<>();
    private int numberOfUserLocations;

    public LocationList(LocationService locationService, List<UserLocation> networkServiceResponseLocations, boolean detailed) {
        this.networkServiceResponseLocations = networkServiceResponseLocations;
        this.detailed = detailed;

        numberOfUserLocations = networkServiceResponseLocations.size();
        single = numberOfUserLocations == 1;

        for (var i = 0; i < numberOfUserLocations; i++) {
            var userLocation = networkServiceResponseLocations.get(i);
            var index = userLocation.getIndex();

            var locationStatus = userLocation.getStatus();
            if (locationStatus.compareTo(globalStatus) > 0) {
                globalStatus = locationStatus;
            }

            var locationLastActivity = userLocation.getLastActivity();
            if (locationLastActivity.isAfter(globalLastActivity)) {
                globalLastActivity = userLocation.getLastActivity();
            }

            var ip = userLocation.getIp();
            var mac = userLocation.getMac();

            var fullLocation = locationService.get(index);
            locationsNames.add(fullLocation.getName());

            attachmentList.add(fullLocation.toAttachment(i, userLocation, detailed, single));
        }
    }

}
