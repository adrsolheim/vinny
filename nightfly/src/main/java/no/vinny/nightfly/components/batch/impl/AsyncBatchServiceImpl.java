package no.vinny.nightfly.components.batch.impl;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.*;
import no.vinny.nightfly.components.batch.domain.BatchDTO;
import no.vinny.nightfly.components.batch.domain.Mapper;
import no.vinny.nightfly.components.batch.AsyncBatchRepository;
import no.vinny.nightfly.components.batch.AsyncBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service("AsyncBatchService")
public class AsyncBatchServiceImpl implements AsyncBatchService {

    private final AsyncBatchRepository asyncBatchRepository;
    private final Mapper.BatchToDTO batchToDTO;

    @Autowired
    public AsyncBatchServiceImpl(AsyncBatchRepository asyncBatchRepository, Mapper.BatchToDTO batchToDTO) {
        this.asyncBatchRepository = asyncBatchRepository;
        this.batchToDTO = batchToDTO;
    }

    @Override
    public Mono<BatchDTO> get(Long id) {
        log.info("Fetching batch of id {}..", id);
        return asyncBatchRepository.findById(id).delayElement(Duration.ofSeconds(3)).map(batchToDTO);
    }

    @Override
    public Mono<BatchDTO> getByBrewfatherId(String id) {
        return asyncBatchRepository
                .findByBrewfatherId(id)
                .singleOrEmpty()
                .map(batchToDTO)
                .doOnSuccess((val) -> log.info("Found 1 element matching brewfatherId={}", id));
        //result.publish().autoConnect(1).hasElement().subscribe((hasElement) -> {
        //    log.info("Found {} elements matching brewfatherId={}", hasElement ? "1" : "no", id);
        //});

    }


    @Override
    public Mono<Long> add(BatchDTO batch) {
        return asyncBatchRepository.save(batch);
    }

    @Override
    public Mono<Long> count() {
        return asyncBatchRepository.count();
    }

    @Override
    public Flux<BatchDTO> getAll(Pageable pageable) {
        Flux<BatchDTO> result = asyncBatchRepository.findAll(pageable).delayElements(Duration.ofMillis(200)).map(batchToDTO);
        result.publish().autoConnect(1).count().subscribe((size) -> log.info("Fetching {} batches from database", size));

        return result;
    }

    @Override
    public Mono<Long> delete(Long id) {
       return asyncBatchRepository.deleteById(id);
    }

    @Override
    public Mono<BatchDTO> update(Long id, BatchDTO dto) {
        return asyncBatchRepository.findById(id)
                .flatMap(data -> data == null ? Mono.error(new RuntimeException("Cannot update batch. Batch does not exist"))
                        : asyncBatchRepository.update(dto).map(batchToDTO));
    }

    @Override
    public Mono<BatchDTO> replace(Long id, BatchDTO batch) {
        return null;
    }
}
