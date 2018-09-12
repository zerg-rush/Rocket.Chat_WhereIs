package pl.aszul.hot.rwb.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import java.time.format.DateTimeFormatter;

@Log4j2
public class AppConfig {

    public static final String WHITE_COLOR = "#FFFFFF";

    public static final String REST_ENTRY_POINT = "/**";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String USERNAME_CLAIM_NAME = "username";
    public static final String SCOPES_CLAIM_NAME = "scopes";

    public static final String CONSTRAINTS_JSON_KEY = "errors";
    public static final String VALIDATION_DESCRIPTION_JSON_KEY = "message";
    public static final String ERROR_ID_JSON_KEY = "id";

    public static final String ROCKET_CHAT_URL = "http://192.168.99.100:3000/";

    public static final String NETWORK_SERVICE_URL = "http://whereis-netsvc:8081/whereis/";

    public static final String BACKEND_HOST = "http://localhost:8080/";
    public static final String USER_AVATARS_URL = BACKEND_HOST + "avatars/";
    public static final String LOCATION_MAPS_URL = BACKEND_HOST + "maps/";
    public static final String IMAGES_URL = BACKEND_HOST + "images/";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${file.user.avatars.folder}")
    public String USER_AVATARS_FOLDER;


    @Value("${file.location.maps.folder}")
    public String LOCATION_MAPS_FOLDER;

    @Value("${spring.datasource.url}")
    public String config_jdbc;

    @Value("${jwt.secret}")
    public String config_jwtSecret;

    @Value("${jwt.expiration-time}")
    public String config_jwtExpirationTime;

    @Value("${jwt.revoked-tokens-flush-interval-in-minutes}")
    public String config_jwtRevokedTokensFlushInterval;

    @Value("${jwt.email.activation.expiration-time-in-minutes}")
    public String config_jwtEmailActivationExpirationTime;

    public AppConfig() {
    }

}
