package no.vinny.nightfly.batch.domain;

import lombok.*;
import no.vinny.nightfly.recipe.domain.RecipeDTO;
import no.vinny.nightfly.taphouse.TapStatus;

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
