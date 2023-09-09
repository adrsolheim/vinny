package no.vinny.nightfly.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

public class AuthenticationManager implements ReactiveAuthenticationManager {

    private SupabaseAuthService supabaseAuthService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .map(auth -> auth.getCredentials().toString())
                .map(this::toUser)
                .map(user -> new UsernamePasswordAuthenticationToken(user, authentication.getCredentials().toString(), user.getAuthorities()));
    }

    private String username(String token) {
       return toUser(token).getUsername();
    }

    private SupabaseUser toUser(String token) {
        return supabaseAuthService.user(token);
    }
}
