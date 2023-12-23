package no.vinny.nightfly.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ReactiveJwtAuthenticationConverter implements ServerAuthenticationConverter {

    private final SupabaseAuthService supabaseAuthService;

    public ReactiveJwtAuthenticationConverter(SupabaseAuthService supabaseAuthService) {
        this.supabaseAuthService = supabaseAuthService;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange)
                .flatMap(e -> Mono.justOrEmpty(e.getRequest().getHeaders().getFirst("Authorization")))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(authHeader -> authHeader.substring(7))
                .flatMap(this::from);
                //.flatMap(this::authenticate);
    }

    public Mono<Authentication> from(String token) {
        // TODO: throw exception
        if (token == null) {
            return Mono.empty();
        }
        SupabaseUser user = supabaseAuthService.user(token);
        if (user == null) {
           return Mono.empty();
        }
        log.info("Successfully extracted user {} from JWT", user.getUsername());
        // TODO: wrapper AbstractAuthenticationToken?
        return Mono.just(new UsernamePasswordAuthenticationToken(user, token));
    }
}
