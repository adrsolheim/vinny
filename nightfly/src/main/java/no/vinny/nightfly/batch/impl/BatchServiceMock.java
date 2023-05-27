package no.vinny.nightfly.batch.impl;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.BatchDTO;
import no.vinny.nightfly.batch.BatchService;
import no.vinny.nightfly.batch.BatchStatus;
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
                        .status(BatchStatus.COMPLETED)
                        .build()
        ).delayElement(Duration.ofSeconds(3));
    }

    @Override
    public Mono<Integer> add(BatchDTO batch) {
        return null;
    }

    @Override
    public Flux<BatchDTO> getAll() {
        return null;
    }
}
