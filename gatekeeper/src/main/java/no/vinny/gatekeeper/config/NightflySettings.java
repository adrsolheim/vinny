package no.vinny.gatekeeper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.nightfly")
public class NightflySettings {
    private String clientSecret;
}
