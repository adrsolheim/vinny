package no.vinny.gatekeeper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "credentials.sunflower")
public class SunflowerSettings {
    private String clientSecret;
}
