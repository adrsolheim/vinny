package no.vinny.nightfly.batch;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
