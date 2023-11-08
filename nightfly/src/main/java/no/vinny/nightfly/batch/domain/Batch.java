package no.vinny.nightfly.batch.domain;

import lombok.*;
import no.vinny.nightfly.recipe.domain.RecipeDTO;

@Data
@AllArgsConstructor
@Builder
public class Batch {
    private final Long id;
    private final String brewfatherId;
    private final String name;
    private final BatchStatus status;
    private RecipeDTO recipe;
    // TODO: filter batches for display on frontend
}
