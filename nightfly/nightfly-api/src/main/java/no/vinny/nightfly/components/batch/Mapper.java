package no.vinny.nightfly.components.batch;

import no.vinny.nightfly.brewfather.domain.BatchJson;
import no.vinny.nightfly.brewfather.domain.RecipeJson;
import no.vinny.nightfly.domain.batch.Batch;
import no.vinny.nightfly.domain.Recipe;
import org.springframework.stereotype.Service;

import java.util.function.Function;

public class Mapper {

    @Service
    public static class BatchJsonToDTO implements Function<BatchJson, Batch> {
        @Override
        public Batch apply(BatchJson batch) {
            return Batch.builder()
                    .brewfatherId(batch.getBrewfatherId())
                    .name(batch.getRecipe() == null ? null : batch.getRecipe().getName())
                    .status(batch.getStatus() == null ? null : batch.getStatus())
                    .build();
        }
    }

    @Service
    public static class RecipeJsonToDTO implements Function<RecipeJson, Recipe> {

        @Override
        public Recipe apply(RecipeJson recipeJson) {
            return Recipe.builder()
                    .brewfatherId(recipeJson.getBrewfatherId())
                    .name(recipeJson.getName())
                    .build();
        }
    }
}
