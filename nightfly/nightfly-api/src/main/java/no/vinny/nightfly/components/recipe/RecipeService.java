package no.vinny.nightfly.components.recipe;

import no.vinny.nightfly.components.common.sync.SyncEntity;
import no.vinny.nightfly.domain.Recipe;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    Optional<Recipe> get(Long id);
    Optional<Recipe> getByBrewfatherId(String id);
    int add(Recipe batch);
    List<Recipe> getAll(Pageable pageable);
    Long count();
    int delete(Long id);
    Recipe update(Recipe batch);
    Recipe upsert(Recipe batch);
    Recipe replace(Recipe batch);

    Optional<SyncEntity> getLastImportedEntity();
    int importRecipe(String recipe);
}
