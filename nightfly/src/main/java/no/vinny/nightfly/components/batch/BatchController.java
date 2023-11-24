package no.vinny.nightfly.components.batch;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.batch.domain.BatchDTO;
import no.vinny.nightfly.config.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService batchService;
    private final Pagination pagination;

    @Autowired
    public BatchController(BatchService batchService, Pagination pagination) {
        this.batchService = batchService;
        this.pagination = pagination;
    }

    // TODO: return message if missing
    @GetMapping("/{id}")
    public BatchDTO batch(@PathVariable Long id) {
        return batchService.get(id).orElse(null);
    }

    @GetMapping("/brewfather/{id}")
    public BatchDTO batch(@PathVariable String id) {
        return batchService.getByBrewfatherId(id).orElse(null);
    }

    @GetMapping
    public List<BatchDTO> batches() {
        Pageable pageable = PageRequest.of(0, pagination.getPageSize());
        return batchService.getAll(pageable);
    }

    @GetMapping("/count")
    public Long count() {
        return batchService.count();
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        log.info("Request for deleting batch id {}", id);
        return batchService.delete(id);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public int create(@RequestBody BatchDTO batch) {
        return batchService.add(batch);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public BatchDTO replace(@PathVariable Long id, @RequestBody BatchDTO replacementBatch) {
        replacementBatch.setId(id);
        log.info("Replacement request for id {}. Batch: {}", id, replacementBatch);
        return batchService.replace(replacementBatch);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public BatchDTO update(@PathVariable Long id, @RequestBody BatchDTO updateBatch) {
        updateBatch.setId(id);
        log.info("Update request for id {}. Batch: {}", id, updateBatch);
        return batchService.update(updateBatch);
    }
}
