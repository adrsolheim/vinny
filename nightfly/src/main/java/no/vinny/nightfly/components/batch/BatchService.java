package no.vinny.nightfly.components.batch;

import no.vinny.nightfly.components.batch.domain.Batch;
import no.vinny.nightfly.components.batch.domain.BatchUnit;
import no.vinny.nightfly.components.batch.domain.BatchUnitDTO;
import no.vinny.nightfly.components.batch.domain.VolumeStatus;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BatchService {
   Optional<Batch> get(Long id);
   Optional<BatchUnit> getBatchUnit(Long batchUnitId);
   Optional<Batch> getByBrewfatherId(String id);
   int add(Batch batch);
   List<Batch> getAll();
   Long count();
   int delete(Long id);
   Batch update(Batch batch);
   Batch upsert(Batch batch);
   Batch replace(Batch batch);
   BatchUnit update(BatchUnit batchUnit);
   List<BatchUnitDTO> findAllBy(Set<Long> batchIds, VolumeStatus volumeStatus, Set<TapStatus> excludeTapStatus);

}
