package pl.aszul.hot.rwb.model;

public enum LocationType {

    LAN("LAN", "LAN network"),
    WIFI("WIFI", "WiFi network");

    private String code;
    private String value;

    LocationType(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
