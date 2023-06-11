package no.vinny.nightfly.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        return batchService.get(id);
    }

    @GetMapping("/brewfather/{id}")
    public Mono<BatchDTO> batch(@PathVariable String id) {
        return batchService.getByBrewfatherId(id);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BatchDTO> batches() {
        return batchService.getAll();
    }

    @GetMapping("/sse")
    public Flux<ServerSentEvent<BatchDTO>> streamEvents() {
        return batchService.getAll()
                .map(batch -> ServerSentEvent.<BatchDTO> builder()
                        .id(batch.getBrewfatherId())
                        .event("periodic-event")
                        .data(batch)
                        .build());
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody BatchDTO batch) {
    }

}
