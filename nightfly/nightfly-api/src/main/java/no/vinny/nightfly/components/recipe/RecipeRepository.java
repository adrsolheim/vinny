package no.vinny.nightfly.components.recipe;

import no.vinny.nightfly.components.common.sync.SyncEntity;
import no.vinny.nightfly.domain.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository {
    int insert(Recipe recipe);

    int delete(Long id);

    void update(Recipe recipe);

    Long count();

    Optional<Recipe> findById(Long id);

    Optional<Recipe> findByBrewfatherId(String id);

    List<Recipe> findAll();

    List<Recipe> findAllByBrewfatherIds(List<String> brewfatherIds);

    int importRecipe(String recipe);
    Optional<SyncEntity<Recipe>> getLastImportedEntity();

}
