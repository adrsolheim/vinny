package no.vinny.nightfly.components.batch;

import no.vinny.nightfly.components.batch.domain.Batch;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;

import java.util.List;
import java.util.Optional;

public interface BatchRepository {
    int insert(Batch batch);

    int delete(Long id);

    void update(Batch batch);

    Long count();

    Optional<Batch> findById(Long id);

    Optional<Batch> findByBrewfatherId(String id);

    List<Batch> findAll();

    List<Batch> findByTapStatus(TapStatus status);
}