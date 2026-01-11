package no.vinny.nightfly.components.common.sync;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.recipe.RecipeService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SyncService {
    private final BatchService batchService;
    private final RecipeService recipeService;

    public SyncService(BatchService batchService, RecipeService recipeService) {
        this.batchService = batchService;
        this.recipeService = recipeService;
    }

    public void syncAll() {
        log.info("========== Syncing Batches ==========");
        batchService.syncBatches();
    }
}
