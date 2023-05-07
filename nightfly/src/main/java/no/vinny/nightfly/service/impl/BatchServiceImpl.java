package no.vinny.nightfly.service.impl;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.domain.data.Batch;
import no.vinny.nightfly.domain.data.BatchStatus;
import no.vinny.nightfly.domain.dto.BatchDTO;
import no.vinny.nightfly.domain.util.BatchMapping;
import no.vinny.nightfly.repository.BatchRepository;
import no.vinny.nightfly.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@Slf4j
@Service
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;

    @Autowired
    public BatchServiceImpl(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    @Override
    public Mono<BatchDTO> getBatch(Long id) {
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
    public Mono<Integer> addBatch(BatchDTO batch) {
        Mono<Integer> result = Mono.just(batchRepository.insert(batch));
        log.info("inserted {} to the database, {} rows updated", batch, result);
        return result;
    }

    @Override
    public Flux<BatchDTO> getAllBatches() {
        log.info("Fetching all batches from database..");
        List<Batch> result = batchRepository.findAll();
        if (result.size() == 0) {
            return Flux.empty();
        }
        return Flux.fromIterable(result.stream().map(BatchMapping::batchToDTO).collect(Collectors.toList()));
    }
}
