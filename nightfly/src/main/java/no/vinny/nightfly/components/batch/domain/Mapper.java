package no.vinny.nightfly.components.batch.domain;

import no.vinny.nightfly.brewfather.domain.BatchJson;
import no.vinny.nightfly.brewfather.domain.RecipeJson;
import no.vinny.nightfly.components.recipe.domain.RecipeDTO;
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
    public static class RecipeJsonToDTO implements Function<RecipeJson, RecipeDTO> {

        @Override
        public RecipeDTO apply(RecipeJson recipeJson) {
            return RecipeDTO.builder()
                    .brewfatherId(recipeJson.getBrewfatherId())
                    .name(recipeJson.getName())
                    .build();
        }
    }
}
