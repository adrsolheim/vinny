package no.vinny.nightfly.recipe.impl;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.recipe.domain.RecipeDTO;
import no.vinny.nightfly.recipe.RecipeRepository;
import no.vinny.nightfly.recipe.RecipeService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Optional<RecipeDTO> get(Long id) {
        return recipeRepository.findById(id);
    }

    @Override
    public Optional<RecipeDTO> getByBrewfatherId(String id) {
        return recipeRepository.findByBrewfatherId(id);
    }

    @Override
    public int add(RecipeDTO recipe) {
        Optional<RecipeDTO> existingRecipe = getByBrewfatherId(recipe.getBrewfatherId());
        return existingRecipe.isPresent() ? 0 : recipeRepository.insert(recipe);
    }

    @Override
    public List<RecipeDTO> getAll(Pageable pageable) {
        return recipeRepository.findAll();
    }

    @Override
    public Long count() {
        return recipeRepository.count();
    }

    @Override
    public int delete(Long id) {
        return recipeRepository.delete(id);
    }

    @Override
    public RecipeDTO update(RecipeDTO recipe) {
        if (recipe.getId() == null) {
            throw new IllegalArgumentException("Batch id must be present in order to find and update batch");
        }
        Optional<RecipeDTO> existingRecipe = get(recipe.getId());
        if (existingRecipe.isEmpty()) {
            log.info("UPDATE: Batch not found. Skipping update..");
            return null;
        }
        recipeRepository.update(mergeNonNull(recipe, existingRecipe.get()));
        return get(recipe.getId()).orElse(null); // TODO: throw exception with description here
    }

    /**
     * @param update    select all non null properties
     * @param old       select properties missing from update
     *
     * @return          merged recipe with updated and old (remaining, not updated) properties
     */
    private RecipeDTO mergeNonNull(RecipeDTO update, RecipeDTO old) {
        return RecipeDTO.builder()
                .id(old.getId())
                .brewfatherId(update.getBrewfatherId() == null ? old.getBrewfatherId() : update.getBrewfatherId())
                .name(update.getName() == null ? old.getName() : update.getName())
                .build();
    }

    @Override
    public RecipeDTO upsert(RecipeDTO recipe) {
        if (recipe.getId() != null) {
            return update(recipe);
        }
        getByBrewfatherId(recipe.getBrewfatherId()).ifPresent(existing -> recipe.setId(existing.getId()));
        return recipe.getId() != null ?
                update(recipe) : add(recipe) == 1 ?
                getByBrewfatherId(recipe.getBrewfatherId()).orElse(null) : null;
    }

    @Override
    public RecipeDTO replace(RecipeDTO recipe) {
        if (recipe.getId() == null) {
            log.warn("Unable to replace recipe. Missing id {}", recipe);
            return null;
        }
        Optional<RecipeDTO> recipeById = get(recipe.getId());
        Optional<RecipeDTO> recipeByBrewfatherId = getByBrewfatherId(recipe.getBrewfatherId());
        if (recipeByBrewfatherId.isPresent() && !Objects.equals(recipeById.get().getId(), recipeByBrewfatherId.get().getId())) {
            log.warn("Cannot replace update recipe {}. Recipe with brewfather id already exist: {}", recipe.getId(), recipeByBrewfatherId);
            return null;
        }
        recipeRepository.update(recipe);
        return get(recipe.getId()).get();
    }
}
