package no.vinny.nightfly.controller;

import no.vinny.nightfly.domain.data.Batch;
import no.vinny.nightfly.domain.data.BatchStatus;
import no.vinny.nightfly.domain.dto.BatchDTO;
import no.vinny.nightfly.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    @Autowired
    BatchService batchService;

    @GetMapping("/{id}")
    public Mono<BatchDTO> batch(@PathVariable Long id) {
        return batchService.getBatch(id);
        //return Batch.builder()
        //        .id(id)
        //        .brewfatherId("HY27A73dYWZNMxapgE4UdljPtNvDOS")
        //        .name("Test Batch")
        //        .status(BatchStatus.BREWING)
        //        .build();
    }

    @GetMapping()
    public Flux<BatchDTO> batches() {
        return batchService.getBatches();
        //return List.of(
        //        Batch.builder()
        //            .id(Long.valueOf(0))
        //            .brewfatherId("HY27A73dYWZNMxapgE4UdljPtNvDOS")
        //            .name("Test Batch")
        //            .status(BatchStatus.BREWING)
        //            .build(),
        //        Batch.builder()
        //            .id(1L)
        //            .brewfatherId("HY27A73dYWZNMxapgE4UdljPtNvDOO")
        //            .name("Test Batch 2")
        //            .status(BatchStatus.BREWING)
        //            .build()
        //);
    }
}
