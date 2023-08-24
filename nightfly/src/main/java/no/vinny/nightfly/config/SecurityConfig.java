package no.vinny.nightfly.config;

import no.vinny.nightfly.util.RequestExceptionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
//@EnableMethodSecurity TODO: preauth intrusive requests
@Configuration
public class SecurityConfig {

    private final RequestExceptionInterceptor interceptor;

    @Autowired
    public SecurityConfig(RequestExceptionInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .exceptionHandling()
                .authenticationEntryPoint(interceptor)
                .and()
                // TODO: enable csrf
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/api/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/batches").permitAll()
                .pathMatchers(HttpMethod.PUT, "/api/batches/**").permitAll()
                .pathMatchers(HttpMethod.PATCH, "/api/batches/**").permitAll()
                .and()
                .build();
    }
}
