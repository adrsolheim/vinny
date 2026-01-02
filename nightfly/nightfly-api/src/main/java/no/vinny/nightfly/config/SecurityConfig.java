package no.vinny.nightfly.config;

import no.vinny.nightfly.security.JwtInspectionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Profile("!noauth")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .cors(Customizer.withDefaults())
                //.oauth2Login(Customizer.withDefaults())
                //.addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationManager, supabaseAuthService), UsernamePasswordAuthenticationFilter.class)
                //.addFilterAfter(new JwtInspectionFilter(jwtAuthenticationManager), JwtAuthenticationFilter.class)
                .authorizeHttpRequests((request) -> request
                    .requestMatchers(HttpMethod.POST,    "/api/batches/**").hasAnyAuthority("SCOPE_batches.write")
                    .requestMatchers(HttpMethod.PUT,     "/api/batches/**").hasAnyAuthority("SCOPE_batches.write")
                    .requestMatchers(HttpMethod.PATCH,   "/api/batches/**").hasAnyAuthority("SCOPE_batches.write")
                    .requestMatchers(HttpMethod.DELETE,  "/api/batches/**").hasAnyAuthority("SCOPE_batches.write")
                    .requestMatchers(HttpMethod.GET,     "/api/batches/**").hasAnyAuthority("SCOPE_batches.read")
                    .requestMatchers(HttpMethod.POST,    "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.write")
                    .requestMatchers(HttpMethod.PUT,     "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.write")
                    .requestMatchers(HttpMethod.PATCH,   "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.write")
                    .requestMatchers(HttpMethod.DELETE,  "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.write")
                    .requestMatchers(HttpMethod.GET,     "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.read")
                    .requestMatchers(HttpMethod.POST,    "/api/taps/**").hasAnyAuthority("SCOPE_taps.write")
                    .requestMatchers(HttpMethod.PUT,     "/api/taps/**").hasAnyAuthority("SCOPE_taps.write")
                    .requestMatchers(HttpMethod.PATCH,   "/api/taps/**").hasAnyAuthority("SCOPE_taps.write")
                    .requestMatchers(HttpMethod.DELETE,  "/api/taps/**").hasAnyAuthority("SCOPE_taps.write")
                    .requestMatchers(HttpMethod.GET,     "/api/taps/**").hasAnyAuthority("SCOPE_taps.read")
                    .requestMatchers(HttpMethod.GET,     "/api/public/protected").authenticated()
                    .requestMatchers(HttpMethod.GET,     "/api/public").permitAll()
                    .requestMatchers(HttpMethod.GET,     "/login/**").permitAll()
                    .requestMatchers(HttpMethod.GET,     "/swagger-ui/**").permitAll()
                    .requestMatchers(HttpMethod.GET,     "/v3/api-docs/**").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .anyRequest().authenticated())
                .addFilterBefore(new JwtInspectionFilter(), AuthorizationFilter.class)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
                return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Allow localhost for testing
        corsConfiguration.addAllowedOriginPattern("http://127.0.0.1:*");
        corsConfiguration.addAllowedOriginPattern("http://localhost:*");

        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "HEAD", "OPTIONS"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Apply to all endpoints
        return source;
    }
}
