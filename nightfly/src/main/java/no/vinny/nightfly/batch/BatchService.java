package no.vinny.nightfly.batch;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BatchService {
    public Mono<BatchDTO> get(Long id);
    public Mono<BatchDTO> getByBrewfatherId(String id);
    public Mono<Long> add(BatchDTO batch);
    public Flux<BatchDTO> getAll();
    public Mono<Long> count();
    public Mono<Long> deleteAll();
    public Mono<Long> sync();
}
