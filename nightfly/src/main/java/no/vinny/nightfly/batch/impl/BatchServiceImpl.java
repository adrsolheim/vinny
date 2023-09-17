package no.vinny.nightfly.batch.impl;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final Mapper.ToDTO toDTO;

    @Autowired
    public BatchServiceImpl(BatchRepository batchRepository, Mapper.ToDTO toDTO) {
        this.batchRepository = batchRepository;
        this.toDTO = toDTO;
    }

    @Override
    public Mono<BatchDTO> get(Long id) {
        log.info("Fetching batch of id {}..", id);
        return batchRepository.findById(id).delayElement(Duration.ofSeconds(3)).map(toDTO);
    }

    @Override
    public Mono<BatchDTO> getByBrewfatherId(String id) {
        return batchRepository
                .findByBrewfatherId(id)
                .singleOrEmpty()
                .map(toDTO)
                .doOnSuccess((val) -> log.info("Found 1 element matching brewfatherId={}", id));
        //result.publish().autoConnect(1).hasElement().subscribe((hasElement) -> {
        //    log.info("Found {} elements matching brewfatherId={}", hasElement ? "1" : "no", id);
        //});

    }


    @Override
    public Mono<Long> add(BatchDTO batch) {
        return batchRepository.save(batch);
    }

    @Override
    public Mono<Long> count() {
        return batchRepository.count();
    }

    @Override
    public Flux<BatchDTO> getAll(Pageable pageable) {
        Flux<BatchDTO> result = batchRepository.findAll(pageable).delayElements(Duration.ofMillis(200)).map(toDTO);
        result.publish().autoConnect(1).count().subscribe((size) -> log.info("Fetching {} batches from database", size));

        return result;
    }

    @Override
    public Mono<Long> delete(Long id) {
       return batchRepository.deleteById(id);
    }

    @Override
    public Mono<Long> deleteAll() {
        return batchRepository.deleteAll();
    }

    // TODO: Sync mechanism with brewfather
    @Override
    public Mono<Long> sync() {
       return null;
    }

    @Override
    public Mono<BatchDTO> update(Long id, BatchDTO dto) {
        return batchRepository.findById(id)
                .flatMap(data -> data == null ? Mono.error(new RuntimeException("Cannot update batch. Batch does not exist"))
                        : batchRepository.update(dto).map(toDTO));
    }

    @Override
    public Mono<BatchDTO> replace(Long id, BatchDTO batch) {
        return null;
    }
}
