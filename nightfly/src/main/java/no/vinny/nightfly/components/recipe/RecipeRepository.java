package no.vinny.nightfly.components.recipe;

import no.vinny.nightfly.components.recipe.domain.Recipe;

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
}
