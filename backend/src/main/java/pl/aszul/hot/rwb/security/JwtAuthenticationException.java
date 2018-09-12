package pl.aszul.hot.rwb.security;

import org.springframework.security.core.AuthenticationException;

class JwtAuthenticationException extends AuthenticationException {

    JwtAuthenticationException(String msg) {
        super(msg);
    }

}
