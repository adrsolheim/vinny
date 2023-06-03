package no.vinny.nightfly.batch;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BatchService {
    public Mono<BatchDTO> get(Long id);
    public Mono<Integer> add(BatchDTO batch);
    public Flux<BatchDTO> getAll();
}