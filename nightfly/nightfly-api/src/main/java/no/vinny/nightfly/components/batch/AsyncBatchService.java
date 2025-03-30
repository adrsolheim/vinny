package no.vinny.nightfly.components.batch;

import no.vinny.nightfly.domain.batch.Batch;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AsyncBatchService {
   Mono<Batch> get(Long id);
   Mono<Batch> getByBrewfatherId(String id);
   Mono<Long> add(Batch batch);
   Flux<Batch> getAll(Pageable pageable);
   Mono<Long> count();
   Mono<Long> delete(Long id);
   Mono<Batch> update(Long id, Batch batch);
   Mono<Batch> replace(Long id, Batch batch);

}
