package no.vinny.nightfly.batch;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BatchRepository extends Repository<Batch, Long> {
    Mono<Long> save(BatchDTO batch);
    Flux<Batch> findAll(Pageable pageable);
    Mono<Long> deleteAll();
    Mono<Long> deleteById(Long id);
    Mono<Batch> findById(Long id);
    Flux<Batch> findByBrewfatherId(String brewfatherId);
    Mono<Long> count();
}
