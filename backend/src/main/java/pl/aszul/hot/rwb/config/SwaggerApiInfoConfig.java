package pl.aszul.hot.rwb.config;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;

public final class SwaggerApiInfoConfig {

    private static final String TITLE = "Rocket.Chat WhereIs bot providing location data";
    private static final String DESC = "NCDC House Of Talents 2018";
    private static final String VERSION = "1.0";

    private SwaggerApiInfoConfig() {
    }

    static ApiInfo createApiInfo() {
        return new ApiInfoBuilder().title(TITLE).description(DESC).version(VERSION).build();
    }

    public static final class Operations {

        public static final String NOT_FOUND = "Resource with ID was not found";
        public static final String CREATED = "New resource successfully created";
        public static final String TOKEN_GENERATED = "Successfully generated token";
        public static final String UPDATED = "Resource successfully updated";
        public static final String DELETED = "Resource successfully deleted";
        public static final String SUCCESS = "Successfully retrieved resource";
        public static final String LOGOUT = "Successfully logged out";
        public static final String VALIDATION_ERROR = "Token validation error";
        public static final String BAD_REQUEST = "Incorrect request parameters";
        public static final String UNAUTHORIZED = "Authentication is required";
        public static final String REVOKED = "TokenNew that has been revoked";
        public static final String FORBIDDEN = "You are not authorized to do this";
        public static final String BAD_USER_PASS = "Incorrect email and/or password";

        private Operations() {
        }

    }

}
