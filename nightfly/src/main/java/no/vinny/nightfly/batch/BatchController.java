package no.vinny.nightfly.batch;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.config.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/batches")
public class BatchController {

    // TODO: WebClient
    // TODO: MediaType.TEXT_EVENT_STREAM_VALUE can be used with EventSource api in js
    private final AsyncBatchService asyncBatchService;
    private final Pagination pagination;

    @Autowired
    public BatchController(AsyncBatchService asyncBatchService, Pagination pagination) {
        this.asyncBatchService = asyncBatchService;
        this.pagination = pagination;
    }

    @GetMapping("/{id}")
    public Mono<BatchDTO> batch(@PathVariable Long id) {
        return asyncBatchService.get(id);
    }

    @GetMapping("/brewfather/{id}")
    public Mono<BatchDTO> batch(@PathVariable String id) {
        return asyncBatchService.getByBrewfatherId(id);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BatchDTO> batches() {
        Pageable pageable = PageRequest.of(0, pagination.getPageSize());
        return asyncBatchService.getAll(pageable);
    }

    @GetMapping("/count")
    public Mono<Long> count() {
        return asyncBatchService.count();
    }

    @DeleteMapping("/{id}")
    public Mono<Long> delete(@PathVariable Long id) {
        log.info("Request for deleting batch id {}", id);
        return asyncBatchService.delete(id);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/clear")
    public Mono<Long> deleteAll() {
        return asyncBatchService.deleteAll();
    }

    @GetMapping("/sse")
    public Flux<ServerSentEvent<BatchDTO>> streamEvents() {
        return asyncBatchService.getAll(PageRequest.of(0, pagination.getPageSize()))
                .map(batch -> ServerSentEvent.<BatchDTO> builder()
                        .id(batch.getBrewfatherId())
                        .event("periodic-event")
                        .data(batch)
                        .build());
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Long> create(@RequestBody BatchDTO batch) {
        return asyncBatchService.add(batch);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public Mono<BatchDTO> replace(@PathVariable Long id, @RequestBody BatchDTO replacementBatch) {
        log.info("Replacement request for id {}. Batch: {}", id, replacementBatch);
        return asyncBatchService.replace(id, replacementBatch);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public Mono<BatchDTO> update(@PathVariable Long id, @RequestBody BatchDTO update) {
        BatchDTO updatedBatch = BatchObjectMapper.builder(update).id(id).build();
        log.info("Update request for id {}. Batch: {}", id, updatedBatch);
        return asyncBatchService.update(id, updatedBatch);
    }
}
