package no.vinny.nightfly.harvest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.brewfather.BatchJson;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Slf4j
public class ScheduledSync {

    private final BrewfatherSettings brewfatherSettings;
    private final ObjectMapper objectMapper;

    public ScheduledSync(BrewfatherSettings brewfatherSettings, ObjectMapper objectMapper) {
       this.brewfatherSettings = brewfatherSettings;
       this.objectMapper = objectMapper;
    }

    public void start() {
        log.info("Starting scheduled sync");
        RestClient client = RestClient.create();
        List<BatchJson> batchList = client.get()
                .uri("https://api.brewfather.app/v2/batches")
                .headers(h -> h.setBasicAuth(brewfatherSettings.getUser(), brewfatherSettings.getKey()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(List.class);

        log.info("Response: {}", batchList);
    }
}
