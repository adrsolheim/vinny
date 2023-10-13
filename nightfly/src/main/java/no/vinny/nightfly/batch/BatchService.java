package no.vinny.nightfly.batch;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BatchService {
   BatchDTO get(Long id);
   BatchDTO getByBrewfatherId(String id);
   int add(BatchDTO batch);
   List<BatchDTO> getAll(Pageable pageable);
   Long count();
   int delete(Long id);
   Long deleteAll();
   BatchDTO update(BatchDTO batch);
   BatchDTO replace(Long id, BatchDTO batch);

}
