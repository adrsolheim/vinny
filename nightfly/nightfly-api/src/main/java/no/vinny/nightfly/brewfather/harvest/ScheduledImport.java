package no.vinny.nightfly.brewfather.harvest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.common.sync.SyncEntity;
import no.vinny.nightfly.components.recipe.RecipeService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScheduledImport {

    private final BrewfatherSettings brewfatherSettings;
    private final BatchService batchService;
    private final RecipeService recipeService;
    private final ObjectMapper objectMapper;

    public ScheduledImport(BrewfatherSettings brewfatherSettings,
                           BatchService batchService,
                           RecipeService recipeService) {
       this.brewfatherSettings = brewfatherSettings;
       this.batchService = batchService;
       this.recipeService = recipeService;
       this.objectMapper = new ObjectMapper();
    }

    public void start() {
        log.info("Starting scheduled import..");
        boolean dryRun = true;
        importRecipes(dryRun);
        importBatches(dryRun);
        log.info("Stopping scheduled import");
    }

    private void importRecipes(boolean dryRun) {
        log.info("=== Importing Recipes ===");
        String checkpointOfLastImport = findCheckpointOfLastRecipeImport();
        if (checkpointOfLastImport != null) {
            log.info("Checkpoint of last import: {}", checkpointOfLastImport);
        }
        List<String> importedRecipes = importRecipes(checkpointOfLastImport);
        importedRecipes.forEach(recipeJson -> log.info("IMPORT -> {}", recipeJson));
        if (!dryRun) {
            Integer entitiesImported = importedRecipes.stream()
                    .map(recipeService::importRecipe)
                    .reduce(0, (a, b) -> a + b);
            log.info("Imported {} recipes", entitiesImported);
        }
    }

    private void importBatches(boolean dryRun) {
        log.info("=== Importing Batches" + (dryRun ? " (dry run)" : "") + " ===");
        String checkpointOfLastImport = findCheckpointOfLastBatchImport();
        if (checkpointOfLastImport != null) {
            log.info("Checkpoint of last import: {}", checkpointOfLastImport);
        }
        List<String> importedBatches = importBatches(checkpointOfLastImport);
        importedBatches.forEach(batchJson -> log.info("IMPORT -> {}", batchJson));
        if (!dryRun) {
            Integer entitiesImported = importedBatches.stream()
                    .map(batchService::importBatch)
                    .reduce(0, (a, b) -> a + b);
            log.info("Imported {} batches", entitiesImported);
        }
    }

    private List<String> importRecipes(String checkpoint) {
        StringBuilder params = new StringBuilder("complete=true");
        params.append("&limit=50");
        params.append("&order_by=_timestamp_ms");
        params.append("&order_direction=desc");
        if (checkpoint != null) {
            params.append("&start_after="+checkpoint);
        }
        String recipesResponse = RestClient.create()
                .get()
                .uri("https://api.brewfather.app/v2/recipes?" + params.toString())
                .headers(h -> h.setBasicAuth(brewfatherSettings.getUser(), brewfatherSettings.getKey()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<String>() {
                })
                .getBody();

        return splitJsonArrayIntoObjects(recipesResponse);
    }

    private String findCheckpointOfLastRecipeImport() {
        Optional<SyncEntity> lastImportedEntity = recipeService.getLastImportedEntity();
        if (lastImportedEntity.isEmpty()) {
            return null;
        }
        Long timestamp = lastImportedEntity.get().updated();

        StringBuilder params = new StringBuilder("complete=false");
        params.append("&limit=50");
        params.append("&order_by=_timestamp_ms");
        params.append("&order_direction=desc");
        params.append("&include=_timestamp_ms");
        List<SyncEntity> recipes = RestClient.create()
                .get()
                .uri("https://api.brewfather.app/v2/recipes?" + params.toString())
                .headers(h -> h.setBasicAuth(brewfatherSettings.getUser(), brewfatherSettings.getKey()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<SyncEntity>>() {})
                .getBody();
        Optional<SyncEntity> lastImported = recipes.stream()
                .filter(recipe -> recipe.updated() <= timestamp)
                .max(Comparator.comparing(SyncEntity::updated));

        return lastImported
                .map(SyncEntity::brewfatherId)
                .orElse(null) ;
    }

    private String findCheckpointOfLastBatchImport() {
        Optional<SyncEntity> lastImportedEntity = batchService.getLastImportedEntity();
        if (lastImportedEntity.isEmpty()) {
            return null;
        }
        Long timestamp = lastImportedEntity.get().updated();
        StringBuilder params = new StringBuilder("complete=false");
        params.append("&limit=50");
        params.append("&order_by=_timestamp_ms");
        params.append("&order_direction=desc");
        params.append("&include=_timestamp_ms");
        List<SyncEntity> batches = RestClient.create()
                .get()
                .uri("https://api.brewfather.app/v2/batches?" + params.toString())
                .headers(h -> h.setBasicAuth(brewfatherSettings.getUser(), brewfatherSettings.getKey()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<SyncEntity>>() {
                })
                .getBody();
        Optional<SyncEntity> lastImported = batches.stream()
                .filter(batch -> batch.updated() <= timestamp)
                .max(Comparator.comparing(SyncEntity::updated));

        return lastImported
                .map(SyncEntity::brewfatherId)
                .orElse(null) ;
    }

    private List<String> importBatches(String checkpoint) {
        StringBuilder params = new StringBuilder("complete=true");
        params.append("&limit=50");
        params.append("&order_by=_timestamp_ms");
        params.append("&order_direction=desc");
        if (checkpoint != null) {
            params.append("&start_after="+checkpoint);
        }
        String batchesResponse = RestClient.create()
                .get()
                .uri("https://api.brewfather.app/v2/batches?"+params.toString())
                .headers(h -> h.setBasicAuth(brewfatherSettings.getUser(), brewfatherSettings.getKey()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<String>() {
                })
                .getBody();

        return splitJsonArrayIntoObjects(batchesResponse);
    }

    private List<String> splitJsonArrayIntoObjects(String json) {
        if (json == null || json.isBlank() || "[]".equals(json)) {
            return List.of();
        }

        JsonNode root = null;
        try {
            root = objectMapper.readTree(json);
        } catch (JsonProcessingException ex) {
            log.error("Unable to parse json", ex);
            throw new RuntimeException(ex);
        }
        if (!root.isArray()) {
            log.error("Expected json to be array");
            throw new RuntimeException("Expected json to be array");
        }
        return root.valueStream()
                .map(this::toJsonString)
                .collect(Collectors.toUnmodifiableList());
    }

    private String toJsonString(JsonNode node) {
        try {
            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            log.error("Unable to write json node as string", ex);
            throw new RuntimeException(ex);
        }
    }
}
