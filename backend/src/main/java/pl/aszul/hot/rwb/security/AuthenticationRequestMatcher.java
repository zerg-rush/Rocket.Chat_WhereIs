package pl.aszul.hot.rwb.security;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class AuthenticationRequestMatcher implements RequestMatcher {

    private final OrRequestMatcher skipMatcher;

    public AuthenticationRequestMatcher(List<String> pathsToSkip) {
        final List<RequestMatcher> matcher = pathsToSkip.stream()
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());

        skipMatcher = new OrRequestMatcher(matcher);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return !skipMatcher.matches(request);
    }

}
