package no.vinny.nightfly.batch.impl;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.Batch;
import no.vinny.nightfly.batch.BatchDTO;
import no.vinny.nightfly.batch.BatchService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;


@Slf4j
@Service
public class BatchServiceMock implements BatchService {
    @Override
    public Mono<BatchDTO> get(Long id) {
        log.info("Constructing mock batch of id {}..", id);
        return Mono.just(
                BatchDTO.builder()
                        .brewfatherId("HY27A73dYWZNMxapgE4UdljPtNvDO1")
                        .name("Cold IPA")
                        .status(Batch.Status.COMPLETED.getValue())
                        .build()
        ).delayElement(Duration.ofSeconds(3));
    }

    @Override
    public Mono<Long> add(BatchDTO batch) {
        return null;
    }

    @Override
    public Flux<BatchDTO> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public Mono<BatchDTO> getByBrewfatherId(String id) {
        return null;
    }

    @Override
    public Mono<Long> count() {
        return null;
    }

    @Override
    public Mono<Long> deleteAll() {
        return null;
    }

    @Override
    public Mono<Long> sync() {
        return null;
    }
}

