package no.vinny.nightfly.brewfather.harvest;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.*;
import no.vinny.nightfly.brewfather.domain.BatchJson;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ScheduledSync {

    private final BrewfatherSettings brewfatherSettings;
    private final BatchService batchService;
    private final Mapper.JsonToDTO jsonToDTO;

    public ScheduledSync(BrewfatherSettings brewfatherSettings,
                         BatchService batchService,
                         Mapper.JsonToDTO jsonToDTO) {
       this.brewfatherSettings = brewfatherSettings;
       this.batchService = batchService;
       this.jsonToDTO = jsonToDTO;
    }

    public void start() {
        log.info("Starting scheduled sync..");
        List<BatchJson> syncedBatches = new ArrayList<>();
        importBatches().stream()
                .forEach(batchJson -> {
                    if (sync(batchJson)) {
                        syncedBatches.add(batchJson);
                        log.info("SYNC -> {}", batchJson);
                    }
                });
        log.info("Synced {} batches", syncedBatches.size());
        log.info("Stopping scheduled sync");
    }

    private List<BatchJson> importBatches() {
        return RestClient.create()
                .get()
                .uri("https://api.brewfather.app/v2/batches")
                .headers(h -> h.setBasicAuth(brewfatherSettings.getUser(), brewfatherSettings.getKey()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<BatchJson>>() {})
                .getBody();
    }

    private boolean sync(BatchJson batchJson) {
        Optional<BatchDTO> byBrewfatherId = batchService.getByBrewfatherId(batchJson.getBrewfatherId());
        if (byBrewfatherId.isEmpty()) {
            batchService.add(jsonToDTO.apply(batchJson));
            return true;
        }
        BatchDTO existingBatch = byBrewfatherId.get();
        BatchDTO updatedBatch = jsonToDTO.apply(batchJson);
        updatedBatch.setId(existingBatch.getId());
        BatchDTO updateResult = batchService.update(updatedBatch);
        return !Objects.equals(existingBatch, updateResult);
    }
}
