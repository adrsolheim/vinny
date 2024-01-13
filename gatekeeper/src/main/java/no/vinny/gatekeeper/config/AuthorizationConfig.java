package no.vinny.gatekeeper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false) // bean methods behave like plain method calls, creating new instances each time they are called
public class AuthorizationConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest.anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .build();
    }
}
