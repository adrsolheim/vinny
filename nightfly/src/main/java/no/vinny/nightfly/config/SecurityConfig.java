package no.vinny.nightfly.config;

import no.vinny.nightfly.security.AuthenticationManager;
import no.vinny.nightfly.security.JwtAuthenticationConverter;
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

@EnableWebFluxSecurity
//@EnableMethodSecurity TODO: preauth intrusive requests
@Configuration
public class SecurityConfig {

    private final RequestExceptionInterceptor interceptor;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Autowired
    public SecurityConfig(RequestExceptionInterceptor interceptor, JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.interceptor = interceptor;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Bean
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
                .pathMatchers(HttpMethod.POST, "/api/batches").permitAll()
                .pathMatchers(HttpMethod.PUT, "/api/batches/**").permitAll()
                .pathMatchers(HttpMethod.PATCH, "/api/batches/**").permitAll()
                .pathMatchers(HttpMethod.DELETE, "/api/batches/**").permitAll()
                .anyExchange().permitAll()
                .and()
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new AuthenticationManager();
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationWebFilter(authenticationManager);
    }
}
