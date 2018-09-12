package pl.aszul.hot.rwb.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static pl.aszul.hot.rwb.config.AppConfig.AUTHORIZATION_HEADER;
import static pl.aszul.hot.rwb.config.AppConfig.TOKEN_PREFIX;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationFailureHandler failureHandler;

    public TokenAuthenticationFilter(AuthenticationFailureHandler failureHandler, AuthenticationManager authenticationManager,
                                     RequestMatcher requestMatcher) {
        super(requestMatcher);
        this.setAuthenticationManager(authenticationManager);
        this.failureHandler = failureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        return getAuthenticationManager().authenticate(
                getJwtAuthenticationToken(request.getHeader(AUTHORIZATION_HEADER)));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        final var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {

        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    private JwtAuthenticationToken getJwtAuthenticationToken(String header) {
        if (header == null) {
            return new JwtAuthenticationToken(null);
        }
        if (!header.startsWith(TOKEN_PREFIX)) {
            throw new JwtAuthenticationException("Incorrect header");
        }

        final var token = header.substring(TOKEN_PREFIX.length(), header.length());
        return new JwtAuthenticationToken(token);
    }

}
