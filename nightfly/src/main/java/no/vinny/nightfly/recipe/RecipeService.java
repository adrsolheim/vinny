package no.vinny.nightfly.recipe;

import no.vinny.nightfly.batch.BatchDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    Optional<RecipeDTO> get(Long id);
    Optional<RecipeDTO> getByBrewfatherId(String id);
    int add(RecipeDTO batch);
    List<RecipeDTO> getAll(Pageable pageable);
    Long count();
    int delete(Long id);
    RecipeDTO update(RecipeDTO batch);
    RecipeDTO upsert(RecipeDTO batch);
    RecipeDTO replace(RecipeDTO batch);
}
