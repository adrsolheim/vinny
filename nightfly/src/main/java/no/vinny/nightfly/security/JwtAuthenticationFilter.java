package no.vinny.nightfly.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final SupabaseAuthService supabaseAuthService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SupabaseAuthService supabaseAuthService) {
        super(authenticationManager);
        this.supabaseAuthService = supabaseAuthService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request == null) {
            return;
        }

        UsernamePasswordAuthenticationToken authentication = extractAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken extractAuthentication(HttpServletRequest request) {
        log.info("Extracting authentication from request: {}", request);
        if (request == null || request.getCookies() == null) {
            log.info("Unable to extract authentication headers from request: {}", request);
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            log.info("Cookie name: {}", cookie.getName());
            log.info("Cookie value: {}", cookie.getValue());
            if ("Authentication".equalsIgnoreCase(cookie.getName())) {
                if (cookie.getValue() == null || !cookie.getValue().startsWith("Bearer ")) {
                    return null;
                }
                String accessToken = cookie.getValue().split(" ")[1];
                SupabaseUser user = supabaseAuthService.user(accessToken);
                return user == null ? null : new UsernamePasswordAuthenticationToken(user, user.getPassword(), new ArrayList<>());
            }
        }
        return null;
    }
}
