package no.vinny.nightfly.components.taphouse.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import no.vinny.nightfly.components.batch.domain.Batch;

@AllArgsConstructor
@Data
public class Tap implements Comparable<Tap> {
    private Long id;
    private Batch batch;

    @Override
    public int compareTo(Tap other) {
        return id.compareTo(other.getId());
    }
}
