package no.vinny.nightfly.components.recipe;

import no.vinny.nightfly.components.recipe.domain.RecipeDTO;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository {
    int insert(RecipeDTO recipe);

    int delete(Long id);

    void update(RecipeDTO recipe);

    Long count();

    Optional<RecipeDTO> findById(Long id);

    Optional<RecipeDTO> findByBrewfatherId(String id);

    List<RecipeDTO> findAll();
}
