package no.vinny.nightfly.components.batch;

import no.vinny.nightfly.components.batch.domain.Batch;

import java.util.List;
import java.util.Optional;

public interface BatchService {
   Optional<Batch> get(Long id);
   Optional<Batch> getByBrewfatherId(String id);
   int add(Batch batch);
   List<Batch> getAll();
   Long count();
   int delete(Long id);
   Batch update(Batch batch);
   Batch upsert(Batch batch);
   Batch replace(Batch batch);

}
