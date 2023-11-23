package no.vinny.nightfly.batch.domain;

import no.vinny.nightfly.brewfather.domain.BatchJson;
import no.vinny.nightfly.brewfather.domain.RecipeJson;
import no.vinny.nightfly.recipe.domain.RecipeDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

public class Mapper {
    @Service
    public static class BatchToDTO implements Function<Batch, BatchDTO> {
        @Override
        public BatchDTO apply(Batch batch) {
            return BatchDTO.builder()
                    .id(batch.getId())
                    .brewfatherId(batch.getBrewfatherId())
                    .name(batch.getName())
                    .status(batch.getStatus() == null ? null : batch.getStatus().getValue())
                    .packaging(batch.getPackaging())
                    .tap(batch.getTap())
                    .recipe(batch.getRecipe())
                    .build();
        }
    }

    @Service
    public static class BatchJsonToDTO implements Function<BatchJson, BatchDTO> {
        @Override
        public BatchDTO apply(BatchJson batch) {
            return BatchDTO.builder()
                    .brewfatherId(batch.getBrewfatherId())
                    .name(batch.getRecipe() == null ? null : batch.getRecipe().getName())
                    .status(batch.getStatus() == null ? null : batch.getStatus().getValue())
                    .build();
        }
    }

    @Service
    public static class ToBatch implements Function<BatchDTO, Batch> {
        @Override
        public Batch apply(BatchDTO batchDTO) {
            return Batch.builder()
                    .id(batchDTO.getId())
                    .brewfatherId(batchDTO.getBrewfatherId())
                    .name(batchDTO.getName())
                    .status(BatchStatus.fromValue(batchDTO.getStatus()))
                    .tapStatus(null)
                    .tap(batchDTO.getTap())
                    .packaging(batchDTO.getPackaging())
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
