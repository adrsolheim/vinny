package no.vinny.nightfly.config;

import no.vinny.nightfly.security.ReactiveJwtAuthenticationManager;
import no.vinny.nightfly.security.ReactiveJwtAuthenticationConverter;
import no.vinny.nightfly.security.SupabaseAuthService;
import no.vinny.nightfly.util.RequestExceptionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

//@EnableMethodSecurity TODO: preauth intrusive requests
public class ReactiveSecurityConfig {

    private final RequestExceptionInterceptor interceptor;
    private final ReactiveJwtAuthenticationConverter reactiveJwtAuthenticationConverter;

    @Autowired
    public ReactiveSecurityConfig(RequestExceptionInterceptor interceptor, ReactiveJwtAuthenticationConverter reactiveJwtAuthenticationConverter) {
        this.interceptor = interceptor;
        this.reactiveJwtAuthenticationConverter = reactiveJwtAuthenticationConverter;
    }

    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationWebFilter authenticationWebFilter) {
        authenticationWebFilter.setServerAuthenticationConverter(reactiveJwtAuthenticationConverter);
        return http
                .exceptionHandling()
                .authenticationEntryPoint(interceptor)
                .and()
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                // TODO: enable csrf
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/api/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/batches").authenticated()
                .pathMatchers(HttpMethod.PUT, "/api/batches/**").authenticated()
                .pathMatchers(HttpMethod.PATCH, "/api/batches/**").authenticated()
                .pathMatchers(HttpMethod.DELETE, "/api/batches/**").authenticated()
                .anyExchange().permitAll()
                .and()
                .build();
    }

    public ReactiveJwtAuthenticationManager authenticationManager(SupabaseAuthService supabaseAuthService) {
        return new ReactiveJwtAuthenticationManager(supabaseAuthService);
    }

    public AuthenticationWebFilter authenticationWebFilter(ReactiveJwtAuthenticationManager reactiveJwtAuthenticationManager) {
        return new AuthenticationWebFilter(reactiveJwtAuthenticationManager);
    }
}
