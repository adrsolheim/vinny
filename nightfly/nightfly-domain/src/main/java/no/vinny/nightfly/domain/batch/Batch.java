package no.vinny.nightfly.domain.batch;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import no.vinny.nightfly.domain.Recipe;

import java.io.Serializable;
import java.util.List;

@Data
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
