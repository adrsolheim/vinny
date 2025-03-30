package no.vinny.nightfly.components.taphouse;

import no.vinny.nightfly.domain.tap.Tap;

import java.util.List;
import java.util.Optional;

public interface TapService {

    Optional<Tap> find(Long tap);
    List<Tap> findAll();
    List<Tap> findActive();
    Tap connectBatch(Long tap, Long batchId);
}
