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
    private final BatchRepo batchRepo;

    @Autowired
    public BatchServiceImpl(BatchRepository batchRepository, BatchRepo batchRepo) {
        this.batchRepository = batchRepository;
        this.batchRepo = batchRepo;
    }

    @Override
    public Mono<BatchDTO> get(Long id) {
        log.info("Fetching batch of id {}..", id);
        return batchRepo.findById(id).delayElement(Duration.ofSeconds(3)).map(BatchObjectMapper::from);
    }

    @Override
    public Mono<BatchDTO> getByBrewfatherId(String id) {
        return batchRepo
                .findByBrewfatherId(id)
                .map(BatchObjectMapper::from)
                .doOnSuccess((val) -> log.info("Found 1 element matching brewfatherId={}", id));
        //result.publish().autoConnect(1).hasElement().subscribe((hasElement) -> {
        //    log.info("Found {} elements matching brewfatherId={}", hasElement ? "1" : "no", id);
        //});

    }


    @Override
    public void add(BatchDTO batch) {
        batchRepository.save(batch)
                .log()
                .subscribe(b -> log.info("Inserted {} to the database", b));
    }

    @Override
    public Mono<Long> count() {
        return batchRepo.count();
    }

    @Override
    public Flux<BatchDTO> getAll() {
        //Flux<BatchDTO> result = batchRepository.findAll().delayElements(Duration.ofMillis(200)).map(BatchObjectMapper::from);
        Flux<BatchDTO> result = batchRepo.findAll().delayElements(Duration.ofMillis(200)).map(BatchObjectMapper::from);
        result.publish().autoConnect(1).count().subscribe((size) -> log.info("Fetching {} batches from database", size));

        return result;
    }


    @Override
    public Mono<Long> deleteAll() {
        return batchRepo.deleteAll();
    }

    // TODO: Sync mechanism with brewfather
    @Override
    public Mono<Long> sync() {
       return null;
    }
}
