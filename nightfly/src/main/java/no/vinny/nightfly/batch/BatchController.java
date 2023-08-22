package no.vinny.nightfly.batch;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.config.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/batches")
public class BatchController {

    // TODO: WebClient
    // TODO: MediaType.TEXT_EVENT_STREAM_VALUE can be used with EventSource api in js
    private final BatchService batchService;
    private final Pagination pagination;

    @Autowired
    public BatchController(BatchService batchService, Pagination pagination) {
        this.batchService = batchService;
        this.pagination = pagination;
    }

    @GetMapping("/{id}")
    public Mono<BatchDTO> batch(@PathVariable Long id) {
        return batchService.get(id);
    }

    @GetMapping("/brewfather/{id}")
    public Mono<BatchDTO> batch(@PathVariable String id) {
        return batchService.getByBrewfatherId(id);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BatchDTO> batches() {
        Pageable pageable = PageRequest.of(0, pagination.getPageSize());
        return batchService.getAll(pageable);
    }

    @GetMapping("/count")
    public Mono<Long> count() {
        return batchService.count();
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/clear")
    public Mono<Long> deleteAll() {
        return batchService.deleteAll();
    }

    @GetMapping("/sse")
    public Flux<ServerSentEvent<BatchDTO>> streamEvents() {
        return batchService.getAll(PageRequest.of(0, pagination.getPageSize()))
                .map(batch -> ServerSentEvent.<BatchDTO> builder()
                        .id(batch.getBrewfatherId())
                        .event("periodic-event")
                        .data(batch)
                        .build());
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Long> create(@RequestBody BatchDTO batch) {
        return batchService.add(batch);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public Mono<BatchDTO> replace(@PathVariable Long id, @RequestBody BatchDTO replacementBatch) {
        log.info("Replacement request for id {}. Batch: {}", id, replacementBatch);
        return batchService.replace(id, replacementBatch);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public Mono<BatchDTO> update(@PathVariable Long id, @RequestBody BatchDTO update) {
        BatchDTO updatedBatch = BatchObjectMapper.builder(update).id(id).build();
        log.info("Update request for id {}. Batch: {}", id, updatedBatch);
        return batchService.update(id, updatedBatch);
    }
}
