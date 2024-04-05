package no.vinny.nightfly.components.recipe;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.recipe.domain.Recipe;
import no.vinny.nightfly.config.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final Pagination pagination;

    @Autowired
    public RecipeController(RecipeService recipeService, Pagination pagination) {
        this.recipeService = recipeService;
        this.pagination = pagination;
    }

    // TODO: return message if missing
    @GetMapping("/{id}")
    public Recipe recipe(@PathVariable Long id) {
        return recipeService.get(id).orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
    }

    @GetMapping("/brewfather/{id}")
    public Recipe recipe(@PathVariable String id) {
        return recipeService.getByBrewfatherId(id).orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
    }

    @GetMapping
    public List<Recipe> recipes() {
        return recipeService.getAll(null);
    }

    @GetMapping("/count")
    public Long count() {
        return recipeService.count();
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        log.info("Request for deleting recipe id {}", id);
        return recipeService.delete(id);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public int create(@RequestBody Recipe recipe) {
        return recipeService.add(recipe);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public Recipe replace(@PathVariable Long id, @RequestBody Recipe replacementRecipe) {
        replacementRecipe.setId(id);
        log.info("Replacement request for id {}. Recipe: {}", id, replacementRecipe);
        return recipeService.replace(replacementRecipe);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public Recipe update(@PathVariable Long id, @RequestBody Recipe updateRecipe) {
        updateRecipe.setId(id);
        log.info("Update request for id {}. Recipe: {}", id, updateRecipe);
        return recipeService.update(updateRecipe);
    }
}
