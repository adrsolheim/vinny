package no.vinny.nightfly.components.taphouse;

import no.vinny.nightfly.domain.tap.Tap;

import java.util.List;

public interface TapRepository {
    Tap find(Long tap);
    List<Tap> findAll();
    int update(Tap tap);
}
