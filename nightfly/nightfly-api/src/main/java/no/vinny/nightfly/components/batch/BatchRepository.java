package no.vinny.nightfly.components.batch;

import no.vinny.nightfly.domain.batch.Batch;
import no.vinny.nightfly.domain.batch.BatchUnit;
import no.vinny.nightfly.domain.tap.TapStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BatchRepository {
    int insert(Batch batch);

    int insertAll(List<BatchUnit> batchUnits);

    int delete(Long id);

    void update(Batch batch);
    void update(BatchUnit batchUnit);

    Long count();

    Optional<Batch> findById(Long id);

    Optional<Batch> findByBrewfatherId(String id);

    List<Batch> findAll();

    List<Batch> findByTapStatus(TapStatus status);

    List<BatchUnit> findBatchUnits(Set<Long> batchIds, Set<TapStatus> excludeTapStatus);

    Optional<BatchUnit> getBatchUnit(Long batchUnitId);

    Optional<Batch> getBatchAndBatchUnit(Long batchUnitId);

}