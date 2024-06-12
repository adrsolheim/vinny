package no.vinny.gatekeeper.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import no.vinny.gatekeeper.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class SecurityConfig {

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
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

}
