package no.vinny.nightfly.components.batch.domain;

import lombok.*;
import no.vinny.nightfly.components.recipe.domain.RecipeDTO;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;

@Data
@AllArgsConstructor
@Builder
public class Batch {
    private final Long id;
    private final String brewfatherId;
    private final String name;
    private final BatchStatus status;
    private final TapStatus tapStatus;
    private final Packaging packaging;
    private RecipeDTO recipe;
    private Long tap;
    // TODO: filter batches for display on frontend
}
