package no.vinny.SimpleClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/error").permitAll();
                    authorize.requestMatchers("/favicon.ico").permitAll();
                    authorize.requestMatchers("/batches").hasAuthority("SCOPE_batches.read");
                    authorize.requestMatchers("/recipes").hasAuthority("SCOPE_recipes.read");
                    authorize.requestMatchers("/**").authenticated();
                })
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .oauth2Login(Customizer.withDefaults())
                .build();
    }
}
