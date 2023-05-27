package no.vinny.nightfly.batch.impl;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;

    @Autowired
    public BatchServiceImpl(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    @Override
    public Mono<BatchDTO> get(Long id) {
        log.info("Fetching batch of id {}..", id);
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
        Mono<Integer> result = Mono.just(batchRepository.insert(batch));
        if(result.block() == 0) {
            log.warn("Failed to insert {} to the database => {} rows updated", batch, result);
        } else {
            log.info("Inserted {} to the database => {} rows updated", batch, result);
        }
        return result;
    }

    @Override
    public Flux<BatchDTO> getAll() {
        List<BatchDTO> result = batchRepository.findAll().stream().map(BatchObjectMapper::from).collect(Collectors.toList());;
        log.info("Fetching {} batches from database", result.size());
        if (result.size() == 0) {
            return Flux.empty();
        }
        return Flux.fromIterable(result).delayElements(Duration.ofMillis(200));
    }
}
