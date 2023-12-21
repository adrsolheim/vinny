package no.vinny.nightfly.config;

import no.vinny.nightfly.security.JwtAuthenticationFilter;
import no.vinny.nightfly.security.JwtAuthenticationManager;
import no.vinny.nightfly.security.SupabaseAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final SupabaseAuthService supabaseAuthService;

    public SecurityConfig(JwtAuthenticationManager jwtAuthenticationManager, SupabaseAuthService supabaseAuthService) {
        this.jwtAuthenticationManager = jwtAuthenticationManager;
        this.supabaseAuthService = supabaseAuthService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf((csrf) -> csrf.disable())
                .addFilter(new JwtAuthenticationFilter(jwtAuthenticationManager, supabaseAuthService))
                .authorizeHttpRequests((request) -> request
                    //.anyRequest().permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/batches").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/batches/**").authenticated()
                    .requestMatchers(HttpMethod.PATCH, "/api/batches/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/batches/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
        ).build();
    }
}
