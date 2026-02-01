package no.vinny.nightfly.components.taphouse;

import no.vinny.nightfly.components.taphouse.api.ConnectBatchRequest;
import no.vinny.nightfly.domain.tap.TapDTO;

import java.util.List;
import java.util.Optional;

public interface TapService {

    Optional<TapDTO> find(Long tap);
    List<TapDTO> findAll();
    List<TapDTO> findActive();
    TapDTO connectBatch(ConnectBatchRequest request);
}
