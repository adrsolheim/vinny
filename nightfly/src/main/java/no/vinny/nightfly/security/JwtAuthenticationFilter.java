package no.vinny.nightfly.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
        if (request.getHeader("Authorization") == null || !request.getHeader("Authorization").startsWith("Bearer ")) {
            return null;
        }
        String accessToken = request.getHeader("Authorization").split(" ")[1];
        SupabaseUser user = supabaseAuthService.user(accessToken);
        return user == null ? null : new UsernamePasswordAuthenticationToken(user, user.getPassword(), new ArrayList<>());
    }
}
