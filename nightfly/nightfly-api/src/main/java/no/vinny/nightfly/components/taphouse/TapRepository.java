package no.vinny.nightfly.components.taphouse;

import no.vinny.nightfly.domain.tap.Tap;
import no.vinny.nightfly.domain.tap.TapDTO;

import java.util.List;

public interface TapRepository {
    TapDTO find(Long tap);
    Tap findById(Long id);
    List<TapDTO> findAll();
    int update(Tap tap);
}
