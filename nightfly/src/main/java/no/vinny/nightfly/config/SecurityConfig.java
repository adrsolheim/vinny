package no.vinny.nightfly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


// TODO: add dev profile to new noSecurity config class
//@Profile("default")
@EnableWebFluxSecurity
//@EnableMethodSecurity TODO: preauth intrusive requests
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                //.cors().disable()
                //.authorizeExchange()
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/api/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/batches").permitAll()
                .pathMatchers(HttpMethod.PUT, "/api/batches/**").permitAll()
                .pathMatchers(HttpMethod.PATCH, "/api/batches/**").permitAll()
                .and()
                //.pathMatchers(HttpMethod.POST, "/api/**").permitAll()
                //.anyExchange().permitAll()
                //.and()
                //.httpBasic()
                //.and()
                .build();
    }
}
