package no.vinny.nightfly.batch;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BatchService {
   Mono<BatchDTO> get(Long id);
   Mono<BatchDTO> getByBrewfatherId(String id);
   Mono<Long> add(BatchDTO batch);
   Flux<BatchDTO> getAll(Pageable pageable);
   Mono<Long> count();
   Mono<Long> delete(Long id);
   Mono<Long> deleteAll();
   Mono<Long> sync();
   Mono<BatchDTO> update(Long id, BatchDTO batch);
   Mono<BatchDTO> replace(Long id, BatchDTO batch);

}
