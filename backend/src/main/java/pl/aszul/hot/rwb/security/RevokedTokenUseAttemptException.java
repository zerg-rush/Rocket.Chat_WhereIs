package pl.aszul.hot.rwb.security;

import org.springframework.security.core.AuthenticationException;

public class RevokedTokenUseAttemptException extends AuthenticationException {

    public RevokedTokenUseAttemptException(String message) {
        super(message);
    }

}
