package no.vinny.gatekeeper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties
public class ServiceSettings {

    Map<String, Setting> credentials;

    public String getSecret(String service) {
        if (credentials == null || !credentials.containsKey(service)) {
            return null;
        }
        return credentials.get(service).clientSecret;
    }

    public record Setting (String clientSecret) {}
}
