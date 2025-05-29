package no.vinny.gatekeeper.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Allow localhost for testing
        corsConfiguration.addAllowedOrigin("http://127.0.0.1:3000");
        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.addAllowedOrigin("http://127.0.0.1:5500");
        corsConfiguration.addAllowedOrigin("http://localhost:5500");


        corsConfiguration.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, etc.)
        corsConfiguration.addAllowedHeader("*"); // Allow all headers

        // Apply CORS configuration to the path where the authorization endpoints reside
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Apply to all endpoints

        return new CorsFilter(source);
    }

    //@Bean
    //public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    //    //return username -> userRepository.findByUsername(username);
    //    List<GrantedAuthority> authorities = Stream.of("batches.read",
    //                                                   "batches.write",
    //                                                   "recipes.read",
    //                                                   "recipes.write",
    //                                                   "taps.read",
    //                                                   "taps.write")
    //                                                .map(SimpleGrantedAuthority::new)
    //                                                .collect(Collectors.toList());
    //    return new InMemoryUserDetailsManager(
    //            User.builder()
    //                    .username("user")
    //                    .password(passwordEncoder().encode("password"))
    //                    .authorities(authorities)
    //                    .build(),
    //            User.builder()
    //                    .username("admin")
    //                    .password(passwordEncoder().encode("password"))
    //                    .authorities(authorities)
    //                    .build(),
    //            User.builder()
    //                    .username("empty")
    //                    .password(passwordEncoder().encode("password"))
    //                    .authorities(List.of())
    //                    .build()
    //    );
    //}

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                        //.requestMatchers("/oauth2/**").permitAll()
                        //.requestMatchers("/login/**").permitAll()
                        //.requestMatchers("/authorize/**").permitAll()
                )
                .cors(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5000", "http://127.0.0.1:5000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

}
