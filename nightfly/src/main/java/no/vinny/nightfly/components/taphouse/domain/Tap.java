package no.vinny.nightfly.components.taphouse.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import no.vinny.nightfly.components.batch.domain.Batch;

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
