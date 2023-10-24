package no.vinny.nightfly.config;

import no.vinny.nightfly.security.AuthenticationManager;
import no.vinny.nightfly.security.JwtAuthenticationConverter;
import no.vinny.nightfly.security.SupabaseAuthService;
import no.vinny.nightfly.util.RequestExceptionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

//@EnableMethodSecurity TODO: preauth intrusive requests
public class ReactiveSecurityConfig {

    private final RequestExceptionInterceptor interceptor;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Autowired
    public ReactiveSecurityConfig(RequestExceptionInterceptor interceptor, JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.interceptor = interceptor;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationWebFilter authenticationWebFilter) {
        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter);
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

    public AuthenticationManager authenticationManager(SupabaseAuthService supabaseAuthService) {
        return new AuthenticationManager(supabaseAuthService);
    }

    public AuthenticationWebFilter authenticationWebFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationWebFilter(authenticationManager);
    }
}
