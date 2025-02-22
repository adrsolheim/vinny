package no.vinny.nightfly.components.taphouse.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import no.vinny.nightfly.components.batch.domain.Batch;
import no.vinny.nightfly.components.batch.domain.BatchUnit;
import no.vinny.nightfly.components.batch.domain.BatchUnitDTO;

@Data
@AllArgsConstructor
public class Tap implements Comparable<Tap> {
    private Long id;
    private boolean active;
    private BatchUnitDTO batchUnit;

    @Override
    public int compareTo(Tap other) {
        return id.compareTo(other.getId());
    }
}
