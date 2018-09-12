package pl.aszul.hot.rwb.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import pl.aszul.hot.rwb.model.Role;
import pl.aszul.hot.rwb.model.rest.UserContext;
import pl.aszul.hot.rwb.user.RevokedTokenService;

import java.util.HashSet;
import java.util.Set;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtParser jwtParser;
    private final RevokedTokenService revokedTokenService;

    @Autowired
    public JwtAuthenticationProvider(JwtParser jwtParser, RevokedTokenService revokedTokenService) {
        this.jwtParser = jwtParser;
        this.revokedTokenService = revokedTokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final var token = (String) authentication.getCredentials();
        if (token == null) {
            return JwtAuthenticationToken.anonymous();
        }

        final var claims = jwtParser.parse(token);
        final var grantedAuthorities = convertToGrantedAuthorities(claims);
        final var userContext = new UserContext(JwtParser.getUsername(claims), JwtParser.getFullname(claims), grantedAuthorities, JwtParser.getExpirationDate(claims));

        if (revokedTokenService.isRevoked(JwtParser.getTokenId(claims))) {
            throw new RevokedTokenUseAttemptException("This token has been revoked");
        } else {
            return new JwtAuthenticationToken(userContext, grantedAuthorities);
        }
    }

    private Set<GrantedAuthority> convertToGrantedAuthorities(Claims claims) {
        Set<GrantedAuthority> set = new HashSet<>();
        for (Object scope : JwtParser.getScopes(claims)) {
            set.add(Role.fromString("" + scope));
        }
        return set;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
