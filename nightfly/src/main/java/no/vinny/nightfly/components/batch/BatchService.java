package no.vinny.nightfly.components.batch;

import no.vinny.nightfly.components.batch.domain.BatchDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BatchService {
   Optional<BatchDTO> get(Long id);
   Optional<BatchDTO> getByBrewfatherId(String id);
   int add(BatchDTO batch);
   List<BatchDTO> getAll(Pageable pageable);
   Long count();
   int delete(Long id);
   BatchDTO update(BatchDTO batch);
   BatchDTO upsert(BatchDTO batch);
   BatchDTO replace(BatchDTO batch);

}
