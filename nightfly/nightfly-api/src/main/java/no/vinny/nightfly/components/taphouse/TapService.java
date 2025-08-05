package no.vinny.nightfly.components.taphouse;

import no.vinny.nightfly.domain.tap.Tap;
import no.vinny.nightfly.domain.tap.TapDTO;

import java.util.List;
import java.util.Optional;

public interface TapService {

    Optional<TapDTO> find(Long tap);
    Optional<Tap> findById(Long id);
    List<TapDTO> findAll();
    List<TapDTO> findActive();
    TapDTO connectBatch(Long tap, Long batchId);
}
