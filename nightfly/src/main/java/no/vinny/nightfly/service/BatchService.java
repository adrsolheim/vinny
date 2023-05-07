package no.vinny.nightfly.service;

import no.vinny.nightfly.domain.dto.BatchDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BatchService {
    public Mono<BatchDTO> getBatch(Long id);
    public Mono<Integer> addBatch(BatchDTO batch);
    public Flux<BatchDTO> getAllBatches();
}
