package pl.aszul.hot.rwb.config;

import pl.aszul.hot.rwb.security.AuthenticationRequestMatcher;

import java.util.Collections;

//TODO do wywalenia
public final class ApiUrl {

    public static final AuthenticationRequestMatcher ALLOW_UNAUTHENTICATED_ACCESS = new AuthenticationRequestMatcher(
            Collections.singletonList("login")
    );

    private ApiUrl() {
    }

}
