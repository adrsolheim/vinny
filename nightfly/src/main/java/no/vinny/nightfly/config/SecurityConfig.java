package no.vinny.nightfly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((request) -> { request
                .requestMatchers(HttpMethod.POST, "/api/batches").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/batches/**").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/api/batches/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/batches/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                .anyRequest().permitAll();
        }).build();
    }
}
