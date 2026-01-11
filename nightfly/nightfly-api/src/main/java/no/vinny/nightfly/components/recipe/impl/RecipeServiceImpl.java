package no.vinny.nightfly.components.recipe.impl;

import no.vinny.nightfly.components.common.error.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.common.sync.SyncEntity;
import no.vinny.nightfly.components.recipe.RecipeRepository;
import no.vinny.nightfly.components.recipe.RecipeService;
import no.vinny.nightfly.domain.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableList;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Optional<Recipe> get(Long id) {
        return recipeRepository.findById(id);
    }

    @Override
    public Optional<Recipe> getByBrewfatherId(String id) {
        return recipeRepository.findByBrewfatherId(id);
    }

    @Override
    public int add(Recipe recipe) {
        Optional<Recipe> existingRecipe = getByBrewfatherId(recipe.getBrewfatherId());
        return existingRecipe.isPresent() ? 0 : recipeRepository.insert(recipe);
    }

    @Override
    public List<Recipe> findAll(Pageable pageable) {
        return recipeRepository.findAll();
    }

    @Override
    public List<Recipe> findAllByBrewfatherIds(List<String> brewfatherIds) {
        return recipeRepository.findAllByBrewfatherIds(brewfatherIds);
    }

    @Override
    public Long count() {
        return recipeRepository.count();
    }

    @Override
    public int delete(Long id) {
        get(id).orElseThrow(() -> new NotFoundException("Recipe not found"));
        return recipeRepository.delete(id);
    }

    @Override
    public Recipe update(Recipe recipe) {
        if (recipe.getId() == null) {
            throw new IllegalArgumentException("Recipe id must be present in order to find and update recipe");
        }
        Optional<Recipe> existingRecipe = get(recipe.getId());
        if (existingRecipe.isEmpty()) {
            log.info("UPDATE: Recipe not found. Skipping update..");
            throw new NotFoundException("Recipe not found");
        }
        recipeRepository.update(mergeNonNull(recipe, existingRecipe.get()));
        return get(recipe.getId()).get(); // TODO: throw exception with description here
    }

    /**
     * @param update    select all non null properties
     * @param old       select properties missing from update
     *
     * @return          merged recipe with updated and old (remaining, not updated) properties
     */
    private Recipe mergeNonNull(Recipe update, Recipe old) {
        return Recipe.builder()
                .id(old.getId())
                .brewfatherId(update.getBrewfatherId() == null ? old.getBrewfatherId() : update.getBrewfatherId())
                .name(update.getName() == null ? old.getName() : update.getName())
                .build();
    }

    @Override
    public Recipe upsert(Recipe recipe) {
        if (recipe.getId() != null) {
            return update(recipe);
        }
        getByBrewfatherId(recipe.getBrewfatherId()).ifPresent(existing -> recipe.setId(existing.getId()));
        return recipe.getId() != null ?
                update(recipe) : add(recipe) == 1 ?
                getByBrewfatherId(recipe.getBrewfatherId()).orElse(null) : null;
    }

    @Override
    public Recipe replace(Recipe recipe) {
        if (recipe.getId() == null) {
            log.warn("Unable to replace recipe. Missing id {}", recipe);
            return null;
        }
        Optional<Recipe> recipeById = get(recipe.getId());
        Optional<Recipe> recipeByBrewfatherId = getByBrewfatherId(recipe.getBrewfatherId());
        if (recipeByBrewfatherId.isPresent() && !Objects.equals(recipeById.get().getId(), recipeByBrewfatherId.get().getId())) {
            log.warn("Cannot replace update recipe {}. Recipe with brewfather id already exist: {}", recipe.getId(), recipeByBrewfatherId);
            return null;
        }
        recipeRepository.update(recipe);
        return get(recipe.getId()).get();
    }

    @Override
    public Optional<SyncEntity<Recipe>> getLastImportedEntity() {
        return recipeRepository.getLastImportedEntity();
    }

    @Override
    public int importRecipe(String recipe) {
        // TODO: Keep 3 latest changes of any given recipe
        return recipeRepository.importRecipe(recipe);
    }

    @Override
    public int syncRecipe(String brewfatherId) {
        Optional<Recipe> existingRecipe = recipeRepository.findByBrewfatherId(brewfatherId);

        return 0;
    }

    @Override
    @Transactional
    public void syncRecipes() {
        // fetch all updates since last sync
        // but keep only latest version of each
        List<SyncEntity<Recipe>> unsyncedRecipes = recipeRepository.findUnsynced();
        List<Recipe> lastVersionOfEachRecipe = unsyncedRecipes.stream()
                .collect(Collectors.groupingBy(SyncEntity::brewfatherId))
                .values().stream()
                .map(recipes -> recipes.stream().max(Comparator.comparing(SyncEntity::updated)).orElse(null))
                .filter(Objects::nonNull)
                .map(SyncEntity::entity)
                .collect(Collectors.toUnmodifiableList());

        Map<String, Recipe> existingRecipes = recipeRepository.findAllByBrewfatherIds(lastVersionOfEachRecipe.stream().map(Recipe::getBrewfatherId).filter(Objects::nonNull).collect(Collectors.toUnmodifiableList())).stream()
                .collect(toMap(Recipe::getBrewfatherId, Function.identity()));
        List<Recipe> updatedRecipes = lastVersionOfEachRecipe.stream()
                .filter(recipe -> existingRecipes.containsKey(recipe.getBrewfatherId()))
                .collect(Collectors.toUnmodifiableList());
        List<Recipe> newRecipes = lastVersionOfEachRecipe.stream()
                .filter(recipe -> !existingRecipes.containsKey(recipe.getBrewfatherId()))
                .collect(Collectors.toUnmodifiableList());

        // update existing recipes
        updatedRecipes.stream()
                .filter(updatedRecipe -> hasChanges(existingRecipes.get(updatedRecipe.getBrewfatherId()), updatedRecipe))
                .map(updatedRecipe -> applyUpdatesFrom(existingRecipes.get(updatedRecipe.getBrewfatherId()), updatedRecipe))
                .forEach(updatedRecipe -> {
                    log.info("[SYNC/{}] Updating existing recipe {}", updatedRecipe.getId(), updatedRecipe);
                    recipeRepository.update(updatedRecipe);
                });
        // add new recipes
        newRecipes.forEach(recipe -> {
                    log.info("[SYNC/null] New recipe {}", recipe);
                    recipeRepository.insert(recipe);
                });

        // mark everything as synced
        recipeRepository.markAsSynced(unsyncedRecipes.stream().map(SyncEntity::id).collect(toUnmodifiableList()));
    }

    private boolean hasChanges(Recipe existing, Recipe updatedRecipe) {
        if (updatedRecipe == null) {
            return false;
        }

        if (!existing.getName().equals(updatedRecipe.getName())) {
            return true;
        }

        return false;
    }

    private Recipe applyUpdatesFrom(Recipe existing, Recipe updatedRecipe) {
        if (updatedRecipe == null) {
            return existing;
        }
        return existing.toBuilder()
                .name(updatedRecipe.getName())
                .build();
    }
}
