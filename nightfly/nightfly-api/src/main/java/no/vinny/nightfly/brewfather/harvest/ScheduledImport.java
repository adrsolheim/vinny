package no.vinny.nightfly.brewfather.harvest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.common.sync.SyncEntity;
import no.vinny.nightfly.components.recipe.RecipeService;
import no.vinny.nightfly.domain.Recipe;
import no.vinny.nightfly.domain.batch.Batch;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        boolean dryRun = false;
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
        ResponseEntity<JsonNode> jsonResponse = RestClient.create()
                .get()
                .uri("https://api.brewfather.app/v2/recipes?" + params)
                .headers(h -> h.setBasicAuth(brewfatherSettings.getUser(), brewfatherSettings.getKey()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(JsonNode.class);

        return toJsonList(jsonResponse.getBody());
    }

    /**
     * Response is ordered by timestamp
     * Use the last imported *timestamp* to find a recipe to use as a 'cutoff'
     *
     * lastImportTs=null
     * scan:    [Recipe(name="Eldon", ts=101), Recipe(name="Kiwi Pilsner", ts=100)]
     * cutoffRecipe=null
     * import:  [Recipe(name="Eldon", ts=101), Recipe(name="Kiwi Pilsner", ts=100)]
     *
     * lastImportTs=101
     * scan:    [Recipe(name="Eldon", ts=105), Recipe(name="Kiwi Pilsner", ts=100)]
     * cutoffRecipe=Recipe(name="Eldon", ts=100) --> ts <= lastImportTs
     * import:  [Recipe(name="Eldon", ts=105)]
     */
    private String findCheckpointOfLastRecipeImport() {
        Optional<SyncEntity<Recipe>> lastImportedEntity = recipeService.getLastImportedEntity();
        if (lastImportedEntity.isEmpty()) {
            return null;
        }
        Long timestamp = lastImportedEntity.get().updated();

        String params = "complete=false" + "&limit=50" +
                "&order_by=_timestamp_ms" +
                "&order_direction=desc" +
                "&include=_timestamp_ms";
        List<SyncEntity> recipes = RestClient.create()
                .get()
                .uri("https://api.brewfather.app/v2/recipes?" + params)
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
        Optional<SyncEntity<Batch>> lastImportedEntity = batchService.getLastImportedEntity();
        if (lastImportedEntity.isEmpty()) {
            return null;
        }
        Long timestamp = lastImportedEntity.get().updated();
        String params = "complete=false" + "&limit=50" +
                "&order_by=_timestamp_ms" +
                "&order_direction=desc" +
                "&include=_timestamp_ms";
        List<SyncEntity> batches = RestClient.create()
                .get()
                .uri("https://api.brewfather.app/v2/batches?" + params)
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
        ResponseEntity<JsonNode> jsonResponse = RestClient.create()
                .get()
                .uri("https://api.brewfather.app/v2/batches?" + params)
                .headers(h -> h.setBasicAuth(brewfatherSettings.getUser(), brewfatherSettings.getKey()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(JsonNode.class);


        return toJsonList(jsonResponse.getBody());
    }

    private List<String> toJsonList(JsonNode root) {
        if (root == null) {
            return List.of();
        }
        if (!root.isArray()) {
            log.error("Expected json to be array");
            throw new RuntimeException("Expected json to be array");
        }

        return root.valueStream()
                .map(JsonNode::toString)
                .collect(Collectors.toUnmodifiableList());
    }
}
