package no.vinny.nightfly.components.batch;

import no.vinny.nightfly.components.common.sync.SyncEntity;
import no.vinny.nightfly.domain.batch.Batch;
import no.vinny.nightfly.domain.batch.BatchUnit;
import no.vinny.nightfly.domain.batch.BatchUnitDTO;
import no.vinny.nightfly.domain.batch.VolumeStatus;
import no.vinny.nightfly.domain.tap.TapStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BatchService {
   Optional<Batch> get(Long id);
   Optional<BatchUnit> getBatchUnit(Long batchUnitId);
   Optional<Batch> getByBrewfatherId(String id);
   Batch create(Batch batch);
   List<Batch> getAll();
   List<Batch> filterBy(Long recipeId, Long tapId);
   Long count();
   int delete(Long id);
   Batch update(Batch batch, String updatedBy);
   Batch upsert(Batch batch, String updatedBy);
   Batch replace(Batch batch);
   BatchUnit update(BatchUnitDTO batchUnit);
   List<BatchUnitDTO> findAllBy(Set<Long> batchIds, VolumeStatus volumeStatus, Set<TapStatus> excludeTapStatus, Boolean kegged);
   Optional<BatchUnitDTO> getBatchUnitById(Long batchUnitId);

   Optional<SyncEntity<Batch>> getLastImportedEntity();
   int importBatch(String batch);
   List<SyncEntity<Batch>> findUnsynced();
   void syncBatches();

}
