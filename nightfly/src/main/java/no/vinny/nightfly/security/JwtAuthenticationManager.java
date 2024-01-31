package no.vinny.nightfly.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationManager implements AuthenticationManager {

    private final SupabaseAuthService supabaseAuthService;

    public JwtAuthenticationManager(SupabaseAuthService supabaseAuthService) {
        this.supabaseAuthService = supabaseAuthService;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null) {
            return null;
        }
        SupabaseUser user = toUser(authentication.getCredentials().toString());
        return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials().toString(), user.getAuthorities());
    }

    private SupabaseUser toUser(String token) {
        return supabaseAuthService.user(token);
    }
}
