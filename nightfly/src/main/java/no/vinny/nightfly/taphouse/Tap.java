package no.vinny.nightfly.taphouse;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import no.vinny.nightfly.batch.domain.Batch;
import no.vinny.nightfly.batch.domain.BatchDTO;

@RequiredArgsConstructor
@Data
public class Tap implements Comparable<Tap> {
    private final Long id;
    private final Batch batch;

    @Override
    public int compareTo(Tap other) {
        return id.compareTo(other.getId());
    }
}
