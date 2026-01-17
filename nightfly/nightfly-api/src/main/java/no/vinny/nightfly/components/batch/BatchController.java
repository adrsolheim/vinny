package no.vinny.nightfly.components.batch;

import no.vinny.nightfly.components.common.error.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.domain.batch.BatchUnitDTO;
import no.vinny.nightfly.domain.batch.VolumeStatus;
import no.vinny.nightfly.domain.tap.TapStatus;
import no.vinny.nightfly.config.Pagination;
import no.vinny.nightfly.domain.batch.Batch;
import no.vinny.nightfly.util.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService batchService;
    private final UserService userService;

    @Autowired
    public BatchController(BatchService batchService, UserService userService) {
        this.batchService = batchService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Batch batch(@PathVariable Long id) {
            return batchService.get(id).orElseThrow(() -> new NotFoundException(String.format("Batch by id=%d not found", id)));
    }

    @GetMapping("/brewfather/{id}")
    public Batch batch(@PathVariable String id) {
        return batchService.getByBrewfatherId(id).orElseThrow(() -> new NotFoundException("Batch not found"));
    }

    @GetMapping
    public List<Batch> batches() {
        return batchService.getAll();
    }

    @GetMapping("/units")
    public List<BatchUnitDTO> batchUnits(@RequestParam(required = false) Set<Long> batchIds,
                                         @RequestParam(required = false) VolumeStatus volumeStatus,
                                         @RequestParam(required = false) Set<TapStatus> excludeTapStatus) {
        return batchService.findAllBy(batchIds, volumeStatus, excludeTapStatus);
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
    public int create(@RequestBody Batch batch) {
        return batchService.add(batch);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public Batch replace(@PathVariable Long id, @RequestBody Batch replacementBatch) {
        replacementBatch.setId(id);
        log.info("Replacement request for id {}. Batch: {}", id, replacementBatch);
        return batchService.replace(replacementBatch);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public Batch update(@PathVariable Long id, @RequestBody Batch updateBatch) {
        updateBatch.setId(id);
        log.info("Update request for id {}. Batch: {}", id, updateBatch);
        return batchService.update(updateBatch);
    }



}
