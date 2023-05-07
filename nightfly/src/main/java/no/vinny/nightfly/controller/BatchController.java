package no.vinny.nightfly.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.domain.data.Batch;
import no.vinny.nightfly.domain.data.BatchStatus;
import no.vinny.nightfly.domain.dto.BatchDTO;
import no.vinny.nightfly.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/batch")
public class BatchController {

    private final BatchService batchService;

    @Autowired
    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping("/{id}")
    public Mono<BatchDTO> batch(@PathVariable Long id) {
        return batchService.getBatch(id);
    }

    @GetMapping()
    public Flux<BatchDTO> batches() {
        return batchService.getAllBatches();
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Integer> create(@RequestBody BatchDTO batch) {
       return batchService.addBatch(batch);
    }
}
