package no.vinny.nightfly.brewfather.harvest;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "brewfather")
public class BrewfatherSettings {
    private String key;
    private String user;
}