package no.vinny.nightfly.components.batch.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import no.vinny.nightfly.components.recipe.domain.Recipe;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Batch implements Serializable {
    private Long id;
    private String brewfatherId;
    private String name;
    @Enumerated(EnumType.STRING)
    private BatchStatus status;
    private Recipe recipe;
    private List<BatchUnit> batchUnits;
    // TODO: filter batches for display on frontend
}
