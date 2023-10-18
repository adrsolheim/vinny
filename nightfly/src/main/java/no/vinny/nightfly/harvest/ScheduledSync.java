package no.vinny.nightfly.harvest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class ScheduledSync {

    private final BrewfatherSettings brewfatherSettings;

    public ScheduledSync(BrewfatherSettings brewfatherSettings) {
       this.brewfatherSettings = brewfatherSettings;
    }

    public void start() {
        log.info("Starting scheduled sync");
        RestClient client = RestClient.create();
        String result = client.get()
                .uri("https://api.brewfather.app/v2/batches")
                .headers(h -> h.setBasicAuth(brewfatherSettings.getUser(), brewfatherSettings.getKey()))
                .retrieve()
                .body(String.class);
        log.info("Response: {}", result);
    }
}
