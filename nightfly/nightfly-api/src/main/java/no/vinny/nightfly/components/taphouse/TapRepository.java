package no.vinny.nightfly.components.taphouse;

import no.vinny.nightfly.domain.tap.TapDTO;

import java.util.List;
import java.util.Optional;

public interface TapRepository {
    Optional<TapDTO> find(Long tap);
    List<TapDTO> findAll();
    int update(TapDTO tap);
}
