package no.vinny.nightfly.batch;

import org.springframework.data.repository.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BatchRepository extends Repository<Batch, Long> {
    public Mono<Long> save(BatchDTO batch);
    public Mono<Long> deleteById(Long id);
    public Mono<Batch> findById(Long id);
    public Flux<Batch> findByBrewfatherId(String brewfatherId);
    public Flux<Batch> findAll();
    public Mono<Long> count();
}
