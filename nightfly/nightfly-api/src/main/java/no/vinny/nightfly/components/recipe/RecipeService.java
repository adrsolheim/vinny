package no.vinny.nightfly.components.recipe;

import no.vinny.nightfly.components.common.sync.SyncEntity;
import no.vinny.nightfly.domain.Recipe;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    Optional<Recipe> get(Long id);
    Optional<Recipe> getByBrewfatherId(String id);
    Recipe create(Recipe recipe);
    List<Recipe> findAll(Pageable pageable);
    List<Recipe> findAllByBrewfatherIds(List<String> brewfatherIds);
    Long count();
    int delete(Long id);
    Recipe update(Recipe recipe);
    Recipe upsert(Recipe recipe);
    Recipe replace(Recipe recipe);

    Optional<SyncEntity<Recipe>> getLastImportedEntity();
    int importRecipe(String recipe);
    int syncRecipe(String brewfatherId);
    void syncRecipes();
}
