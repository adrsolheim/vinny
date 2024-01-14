package no.vinny.nightfly.components.batch.impl;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.batch.BatchRepository;
import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.batch.domain.Batch;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class BatchServiceImpl implements BatchService {

    private final static String REDIS_PREFIX = "Batch";
    private final BatchRepository batchRepository;
    private final RedisTemplate<String, Batch> redisTemplate;

    public BatchServiceImpl(BatchRepository batchRepository, @Qualifier("batch-template") RedisTemplate redisTemplate) {
        this.batchRepository = batchRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Cacheable(cacheNames = {"batch"}, key = "#root.methodName + '[' + #id + ']'")
    public Optional<Batch> get(Long id) {
        return batchRepository.findById(id);
    }
    //public Optional<Batch> get(Long id) {
    //    String redisKey = String.format("%s::%s", REDIS_PREFIX, id);
    //    Batch batch = redisTemplate.opsForValue().get(redisKey);
    //    if (batch != null) {
    //        log.info("Fetched from redis");
    //        return Optional.of(batch);
    //    }
    //    stall();
    //    Optional<Batch> batchById = batchRepository.findById(id);
    //    if (batchById.isPresent()) {
    //        redisTemplate.opsForValue().set(redisKey, batchById.get());
    //    }
    //    return batchById;
    //}

    @Override
    @Cacheable(cacheNames = {"batch"}, key = "#root.methodName + '[' + #brewfatherId + ']'")
    public Optional<Batch> getByBrewfatherId(String brewfatherId) {
        return batchRepository.findByBrewfatherId(brewfatherId);
    }

    @Override
    public int add(Batch batch) {
        Optional<Batch> existingBatch = getByBrewfatherId(batch.getBrewfatherId());
        return existingBatch.isPresent() ? 0 : batchRepository.insert(batch);
    }

    // TODO: Return pagedlist
    @Override
    @Cacheable(cacheNames = {"batches"}, key = "#root.methodName + '[]'")
    public List<Batch> getAll() {
        return batchRepository.findAll();
    }

    //TODO: implement rest
    @Override
    public Long count() {
        return batchRepository.count();
    }

    @Override
    @CacheEvict(cacheNames = "batch", key = "#root.methodName + '[' + #id + ']'")
    // delete cached by brewfatherId
    public int delete(Long id) {
        return batchRepository.delete(id);
    }

    @Override
    // CacheEvict by id and brewfatherId
    public Batch update(Batch batch) {
        if (batch.getId() == null) {
            throw new IllegalArgumentException("Batch id must be present in order to find and update batch");
        }
        Optional<Batch> existingBatch = get(batch.getId());
        if (existingBatch.isEmpty()) {
            log.info("UPDATE: Batch not found. Skipping update..");
            return null;
        }
        batchRepository.update(mergeNonNull(batch, existingBatch.get()));
        return get(batch.getId()).orElse(null); // TODO: throw exception with description here
    }

    /**
     * @param update    select all non null properties
     * @param old       select properties missing from update
     *
     * @return          merged batch eligible to be persisted as a batch update
     */
    private Batch mergeNonNull(Batch update, Batch old) {
        return Batch.builder()
                .id(old.getId())
                .brewfatherId(update.getBrewfatherId() == null ? old.getBrewfatherId() : update.getBrewfatherId())
                .name(update.getName() == null ? old.getName() : update.getName())
                .status(update.getStatus() == null ? old.getStatus() : update.getStatus())
                .build();
    }

    @Override
    public Batch upsert(Batch batch) {
        if (batch.getId() != null) {
            return update(batch);
        }
        getByBrewfatherId(batch.getBrewfatherId()).ifPresent(existing -> batch.setId(existing.getId()));
        return batch.getId() != null ?
                update(batch) : add(batch) == 1 ?
                getByBrewfatherId(batch.getBrewfatherId()).orElse(null) : null;
    }

    @Override
    public Batch replace(Batch batch) {
        if (batch.getId() == null) {
            log.warn("Unable to replace batch. Missing id {}", batch);
            return null;
        }
        Optional<Batch> batchById = get(batch.getId());
        Optional<Batch> batchByBrewfatherId = getByBrewfatherId(batch.getBrewfatherId());
        if (batchByBrewfatherId.isPresent() && !Objects.equals(batchById.get().getId(), batchByBrewfatherId.get().getId())) {
            log.warn("Cannot replace update batch {}. Batch with brewfather id already exist: {}", batch.getId(), batchByBrewfatherId);
            return null;
        }
        batchRepository.update(batch);
        return get(batch.getId()).get();
    }

    private void stall() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
