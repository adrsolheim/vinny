package no.vinny.nightfly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                //.oauth2Login(Customizer.withDefaults())
                //.addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationManager, supabaseAuthService), UsernamePasswordAuthenticationFilter.class)
                //.addFilterAfter(new JwtInspectionFilter(jwtAuthenticationManager), JwtAuthenticationFilter.class)
                .authorizeHttpRequests((request) -> request
                    .requestMatchers(HttpMethod.POST,   "/api/batches/**").hasAnyAuthority("SCOPE_batches.write")
                    .requestMatchers(HttpMethod.PUT,    "/api/batches/**").hasAnyAuthority("SCOPE_batches.write")
                    .requestMatchers(HttpMethod.PATCH,  "/api/batches/**").hasAnyAuthority("SCOPE_batches.write")
                    .requestMatchers(HttpMethod.DELETE, "/api/batches/**").hasAnyAuthority("SCOPE_batches.write")
                    .requestMatchers(HttpMethod.GET,    "/api/batches/**").hasAnyAuthority("SCOPE_batches.read")
                    .requestMatchers(HttpMethod.POST,   "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.write")
                    .requestMatchers(HttpMethod.PUT,    "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.write")
                    .requestMatchers(HttpMethod.PATCH,  "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.write")
                    .requestMatchers(HttpMethod.DELETE, "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.write")
                    .requestMatchers(HttpMethod.GET,    "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.read")
                    .requestMatchers(HttpMethod.POST,   "/api/taps/**").hasAnyAuthority("SCOPE_taps.write")
                    .requestMatchers(HttpMethod.PUT,    "/api/taps/**").hasAnyAuthority("SCOPE_taps.write")
                    .requestMatchers(HttpMethod.PATCH,  "/api/taps/**").hasAnyAuthority("SCOPE_taps.write")
                    .requestMatchers(HttpMethod.DELETE, "/api/taps/**").hasAnyAuthority("SCOPE_taps.write")
                    .requestMatchers(HttpMethod.GET,    "/api/taps/**").hasAnyAuthority("SCOPE_taps.read")
                    .requestMatchers(HttpMethod.GET,    "/api/public/protected").authenticated()
                    .requestMatchers(HttpMethod.GET,    "/api/public").permitAll()
                    .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
                return http.build();
    }
}
