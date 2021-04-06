package io.github.chait.swaggerdemonew.config;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

public class CustomRequestMatcher implements RequestMatcher {
    private List<AntPathRequestMatcher> matchers;

    public CustomRequestMatcher(List<String> matchers) {
        this.matchers = matchers.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList());
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return matchers.stream().anyMatch(a -> a.matches(request));
    }
}
