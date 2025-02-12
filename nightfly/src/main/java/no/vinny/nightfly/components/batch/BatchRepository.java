package no.vinny.nightfly.components.batch;

import no.vinny.nightfly.components.batch.domain.Batch;
import no.vinny.nightfly.components.batch.domain.BatchUnit;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BatchRepository {
    int insert(Batch batch);

    int insertAll(List<BatchUnit> batchUnits);

    int delete(Long id);

    void update(Batch batch);

    Long count();

    Optional<Batch> findById(Long id);

    Optional<Batch> findByBrewfatherId(String id);

    List<Batch> findAll();

    List<Batch> findByTapStatus(TapStatus status);

    List<BatchUnit> findBatchUnits(Set<Long> batchIds, Set<TapStatus> excludeTapStatus);
}