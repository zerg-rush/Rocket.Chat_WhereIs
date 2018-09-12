package pl.aszul.hot.rwb.model;

import static pl.aszul.hot.rwb.config.AppConfig.IMAGES_URL;
import static pl.aszul.hot.rwb.config.AppConfig.USER_AVATARS_URL;

/**
 * Enum type indicating request mode:
 * <p>
 * USER - user asked for data location for specific user,
 * IP - user asked for data location for device with specific IP address,
 * MAC - user asked for data location for device with specific MAC address,
 * NOT_FOUND - user asked for data location for specified user which does not exist,
 * NOT_AUTHORIZED - user is not authorized to use WhereIs command,
 * SYNTAX - syntax error.
 */
public enum WhereIsMode {

    USER("User @", "username: ", USER_AVATARS_URL + "%s" + ".jpg"),
    IP("Device with IP address ", "IP address: ", IMAGES_URL + "IP.png"),
    MAC("Device with MAC address ", "MAC address: ", IMAGES_URL + "MAC.png"),
    NOT_FOUND("", "", ""),
    NOT_AUTHORIZED("", "", ""),
    SYNTAX("", "", "");

    private String titlePrefix;
    private String label;
    private String thumbnailUrl;


    WhereIsMode(String titlePrefix, String label, String thumbnailUrl) {
        this.titlePrefix = titlePrefix;
        this.label = label;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitlePrefix() {
        return titlePrefix;
    }

    public String getLabel() {
        return label;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
