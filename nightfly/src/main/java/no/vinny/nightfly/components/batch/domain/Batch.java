package no.vinny.nightfly.components.batch.domain;

import lombok.*;
import no.vinny.nightfly.components.recipe.domain.Recipe;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;

@Data
@AllArgsConstructor
@Builder
public class Batch {
    private Long id;
    private String brewfatherId;
    private String name;
    private BatchStatus status;
    private TapStatus tapStatus;
    private Packaging packaging;
    private Recipe recipe;
    private Long tap;
    // TODO: filter batches for display on frontend
}
