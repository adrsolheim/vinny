package no.vinny.nightfly.components.taphouse;

import no.vinny.nightfly.components.taphouse.domain.Tap;

import java.util.List;

public interface TapRepository {
    List<Tap> findAll();
}
