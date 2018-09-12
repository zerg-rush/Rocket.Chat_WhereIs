package pl.aszul.hot.rwb.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import pl.aszul.hot.rwb.model.Role;
import pl.aszul.hot.rwb.model.rest.UserContext;

import java.util.Collection;
import java.util.Collections;

class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final UserContext userContext;
    private String token;

    JwtAuthenticationToken(UserContext userContext, Collection<? extends GrantedAuthority> authorities) {
        this(authorities, userContext, null, true);
        eraseCredentials();
    }

    JwtAuthenticationToken(String token) {
        this(null, null, token, false);
    }

    private JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities,
                                   UserContext userContext, String token, boolean isAuthenticated) {
        super(authorities);
        this.userContext = userContext;
        this.token = token;
        super.setAuthenticated(isAuthenticated);
    }

    static Authentication anonymous() {
        return new JwtAuthenticationToken(null, Collections.<GrantedAuthority>singletonList(Role.ANONYMOUS));
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return userContext;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        token = null;
    }

}
