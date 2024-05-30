package no.vinny.nightfly.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtInspectionFilter extends BasicAuthenticationFilter {

    public JwtInspectionFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            log.info("Credentials: {}", authentication.getCredentials());
            log.info("Authorities: {}", authentication.getAuthorities());
            log.info("Details: {}", authentication.getDetails());
            log.info("Principal: {}", authentication.getPrincipal());
        }

        chain.doFilter(request, response);
    }
}
