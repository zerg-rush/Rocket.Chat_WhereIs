package pl.aszul.hot.rwb.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.aszul.hot.rwb.model.Attachment;
import pl.aszul.hot.rwb.model.LocationType;
import pl.aszul.hot.rwb.model.UserLocation;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

import static pl.aszul.hot.rwb.common.Util.durationFormat;
import static pl.aszul.hot.rwb.common.Util.positionToOrdinal;
import static pl.aszul.hot.rwb.config.AppConfig.DATE_TIME_FORMATTER;
import static pl.aszul.hot.rwb.config.AppConfig.LOCATION_MAPS_URL;
import static pl.aszul.hot.rwb.config.AppConfig.WHITE_COLOR;

@Entity
@Table(name = "LOCATION")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DbLocation {

    @Id
    private String index;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private LocationType type;

    private String device;
    private String sn;
    private String mapUrl;

    public boolean hasMap() {
        return !this.mapUrl.isEmpty();
    }

    public Attachment toAttachment(int position, UserLocation userLocation, boolean detailed, boolean single) {
        var index = userLocation.getIndex();
        var type = userLocation.getType();

        var nameText = (this.name == null || this.name.isEmpty() ?
                "" : "location name: " + this.name + "\n");

        var descriptionText = (this.description == null || this.description.isEmpty() ?
                "" : "description: " + this.description + "\n");

        var status = userLocation.getStatus();
        var statusText = single || status == null ?
                "" : "status: " + status + "\n";

        var deviceText = this.device == null || this.device.isEmpty() ?
                "" : "device: " + this.device + "\n";

        var lastActivity = userLocation.getLastActivity();
        var lastActivityText = single || lastActivity == null ?
                "" : "last activity: " + lastActivity.format(DATE_TIME_FORMATTER) + " (" +
                durationFormat(Duration.between(lastActivity, LocalDateTime.now())) + " ago)\n";

        var snText = this.sn == null || this.sn.isEmpty() ?
                "" : "serial number: " + this.sn + "\n";

        var ip = userLocation.getIp();
        var ipText = ip == null ?
                "" : "IP address: " + ip + "\n";

        var mac = userLocation.getMac();
        var macText = mac == null ?
                "" : "MAC address: " + mac + "\n";

        var signalStrength = type == LocationType.WIFI ?
                userLocation.getSignalStrength() : null;
        var signalStrengthText = signalStrength == null ?
                "" : "WiFi signal: " + signalStrength + "\n";

        return new Attachment(
                "",
                "\n" +
                (detailed ? (single ? "" : positionToOrdinal(position) + " ") +
                        "location index: " + this.index + "\n" +
                        nameText + descriptionText + statusText + lastActivityText + deviceText + snText + ipText +
                        macText + signalStrengthText :
                        (single ? "" : positionToOrdinal(position) + " ") + nameText + descriptionText),
                LOCATION_MAPS_URL + index + ".jpg",
                WHITE_COLOR
        );
    }

}
