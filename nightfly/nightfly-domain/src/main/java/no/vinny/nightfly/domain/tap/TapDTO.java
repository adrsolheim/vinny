package no.vinny.nightfly.domain.tap;

import lombok.AllArgsConstructor;
import no.vinny.nightfly.domain.batch.BatchUnitDTO;

@AllArgsConstructor
public class TapDTO implements Comparable<TapDTO> {
    private Long id;
    private boolean active;
    private BatchUnitDTO batchUnit;

    public void setId(Long id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setBatchUnit(BatchUnitDTO batchUnit) {
        this.batchUnit = batchUnit;
    }

    public Long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public BatchUnitDTO getBatchUnit() {
        return batchUnit;
    }

    @Override
    public int compareTo(TapDTO other) {
        return id.compareTo(other.getId());
    }
}
