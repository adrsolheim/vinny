package no.vinny.nightfly.brewfather.harvest;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.*;
import no.vinny.nightfly.batch.domain.BatchDTO;
import no.vinny.nightfly.batch.domain.Mapper;
import no.vinny.nightfly.brewfather.domain.BatchJson;
import no.vinny.nightfly.brewfather.domain.RecipeJson;
import no.vinny.nightfly.recipe.domain.RecipeDTO;
import no.vinny.nightfly.recipe.RecipeService;
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
    private final RecipeService recipeService;
    private final Mapper.BatchJsonToDTO batchJsonToDTO;
    private final Mapper.RecipeJsonToDTO recipeJsonToDTO;

    public ScheduledSync(BrewfatherSettings brewfatherSettings,
                         BatchService batchService,
                         RecipeService recipeService,
                         Mapper.BatchJsonToDTO batchJsonToDTO,
                         Mapper.RecipeJsonToDTO recipeJsonToDTO) {
       this.brewfatherSettings = brewfatherSettings;
       this.batchService = batchService;
       this.recipeService = recipeService;
       this.batchJsonToDTO = batchJsonToDTO;
       this.recipeJsonToDTO = recipeJsonToDTO;
    }

    public void start() {
        log.info("Starting scheduled sync..");
        syncRecipes();
        syncBatches();
        log.info("Stopping scheduled sync");
    }

    private void syncRecipes() {
        log.info("=== Syncing Recipes ===");
        List<RecipeJson> syncedRecipes = new ArrayList<>();
        importRecipes().stream()
                .forEach(recipeJson -> {
                    if (sync(recipeJson)) {
                        syncedRecipes.add(recipeJson);
                        log.info("SYNC -> {}", recipeJson);
                    }
                });
        log.info("Synced {} recipes", syncedRecipes.size());
    }

    private void syncBatches() {
        log.info("=== Syncing Batches ===");
        List<BatchJson> syncedBatches = new ArrayList<>();
        importBatches().stream()
                .forEach(batchJson -> {
                    if (sync(batchJson)) {
                        syncedBatches.add(batchJson);
                        log.info("SYNC -> {}", batchJson);
                    }
                });
        log.info("Synced {} batches", syncedBatches.size());
    }

    private List<RecipeJson> importRecipes() {
        return RestClient.create()
                .get()
                .uri("https://api.brewfather.app/v2/recipes?complete=true")
                .headers(h -> h.setBasicAuth(brewfatherSettings.getUser(), brewfatherSettings.getKey()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<RecipeJson>>() {})
                .getBody();
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

    private boolean sync(RecipeJson recipeJson) {
        Optional<RecipeDTO> byBrewfatherId = recipeService.getByBrewfatherId(recipeJson.getBrewfatherId());
        if (byBrewfatherId.isEmpty()) {
            recipeService.add(recipeJsonToDTO.apply(recipeJson));
            return true;
        }
        RecipeDTO existingRecipe = byBrewfatherId.get();
        RecipeDTO updatedRecipe = recipeJsonToDTO.apply(recipeJson);
        updatedRecipe.setId(existingRecipe.getId());
        RecipeDTO updateResult = recipeService.update(updatedRecipe);
        return !Objects.equals(existingRecipe, updateResult);
    }

    private boolean sync(BatchJson batchJson) {
        Optional<BatchDTO> byBrewfatherId = batchService.getByBrewfatherId(batchJson.getBrewfatherId());
        if (byBrewfatherId.isEmpty()) {
            batchService.add(batchJsonToDTO.apply(batchJson));
            return true;
        }
        BatchDTO existingBatch = byBrewfatherId.get();
        BatchDTO updatedBatch = batchJsonToDTO.apply(batchJson);
        updatedBatch.setId(existingBatch.getId());
        BatchDTO updateResult = batchService.update(updatedBatch);
        return !Objects.equals(existingBatch, updateResult);
    }
}
