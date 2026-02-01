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
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
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
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                //.oauth2Login(Customizer.withDefaults())
                //.addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationManager, supabaseAuthService), UsernamePasswordAuthenticationFilter.class)
                //.addFilterAfter(new JwtInspectionFilter(jwtAuthenticationManager), JwtAuthenticationFilter.class)
                .authorizeHttpRequests((request) -> {
                    requireRead (request, "/api/batches/**", "SCOPE_batches.read");
                    requireWrite(request, "/api/batches/**", "SCOPE_batches.write");
                    requireRead (request, "/api/kegs/**",    "SCOPE_batches.read");
                    requireWrite(request, "/api/kegs/**",    "SCOPE_batches.write");
                    requireRead (request, "/api/recipes/**", "SCOPE_recipes.read");
                    requireWrite(request, "/api/recipes/**", "SCOPE_recipes.write");
                    requireRead (request, "/api/taps/**",    "SCOPE_taps.read");
                    requireWrite(request, "/api/taps/**",    "SCOPE_taps.write");
                    request
                            .requestMatchers(HttpMethod.GET, "/api/public/protected").authenticated()
                            .requestMatchers(HttpMethod.GET, "/api/public").permitAll()
                            .requestMatchers(HttpMethod.GET, "/login/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterBefore(new JwtInspectionFilter(), AuthorizationFilter.class)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
                return http.build();
    }


    private static void requireWrite(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth,
                                     String pattern,
                                     String scope) {
        auth
                .requestMatchers(HttpMethod.POST,   pattern).hasAuthority(scope)
                .requestMatchers(HttpMethod.PUT,    pattern).hasAuthority(scope)
                .requestMatchers(HttpMethod.PATCH,  pattern).hasAuthority(scope)
                .requestMatchers(HttpMethod.DELETE, pattern).hasAuthority(scope);
    }
    private static void requireRead(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth,
                                     String pattern,
                                     String scope) {
        auth.requestMatchers(HttpMethod.GET, pattern).hasAuthority(scope);
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
