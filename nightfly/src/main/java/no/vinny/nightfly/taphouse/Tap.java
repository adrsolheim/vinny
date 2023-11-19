package no.vinny.nightfly.taphouse;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Tap implements Comparable<Tap> {
    private final Long id;
    private final String name;

    @Override
    public int compareTo(Tap other) {
        return id.compareTo(other.getId());
    }
}
