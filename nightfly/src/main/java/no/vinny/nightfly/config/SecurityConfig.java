package no.vinny.nightfly.config;

import no.vinny.nightfly.security.JwtAuthenticationFilter;
import no.vinny.nightfly.security.JwtAuthenticationManager;
import no.vinny.nightfly.security.JwtInspectionFilter;
import no.vinny.nightfly.security.SupabaseAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                //.oauth2Login(Customizer.withDefaults())
                .addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationManager, supabaseAuthService), UsernamePasswordAuthenticationFilter.class)
                //.addFilter(new JwtInspectionFilter(jwtAuthenticationManager))
                .authorizeHttpRequests((request) -> request
                    .requestMatchers(HttpMethod.POST,   "/api/batches/**").hasAnyAuthority("SCOPE_batches.write", "batches.write")
                    .requestMatchers(HttpMethod.PUT,    "/api/batches/**").hasAnyAuthority("SCOPE_batches.write", "batches.write")
                    .requestMatchers(HttpMethod.PATCH,  "/api/batches/**").hasAnyAuthority("SCOPE_batches.write", "batches.write")
                    .requestMatchers(HttpMethod.DELETE, "/api/batches/**").hasAnyAuthority("SCOPE_batches.write", "batches.write")
                    .requestMatchers(HttpMethod.GET,    "/api/batches/**").hasAnyAuthority("SCOPE_batches.read", "batches.read")
                    .requestMatchers(HttpMethod.POST,   "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.write", "recipes.write")
                    .requestMatchers(HttpMethod.PUT,    "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.write", "recipes.write")
                    .requestMatchers(HttpMethod.PATCH,  "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.write", "recipes.write")
                    .requestMatchers(HttpMethod.DELETE, "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.write", "recipes.write")
                    .requestMatchers(HttpMethod.GET,    "/api/recipes/**").hasAnyAuthority("SCOPE_recipes.read", "recipes.read")
                    .requestMatchers(HttpMethod.POST,   "/api/taps/**").hasAnyAuthority("SCOPE_taps.write")
                    .requestMatchers(HttpMethod.PUT,    "/api/taps/**").hasAnyAuthority("SCOPE_taps.write")
                    .requestMatchers(HttpMethod.PATCH,  "/api/taps/**").hasAnyAuthority("SCOPE_taps.write")
                    .requestMatchers(HttpMethod.DELETE, "/api/taps/**").hasAnyAuthority("SCOPE_taps.write")
                    .requestMatchers(HttpMethod.GET,    "/api/taps/**").hasAnyAuthority("SCOPE_taps.read")
                    .anyRequest().authenticated()
        ).build();
    }
}
