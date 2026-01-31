package no.vinny.nightfly.components.batch.impl;

import no.vinny.nightfly.components.common.error.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.batch.BatchRepository;
import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.common.sync.SyncEntity;
import no.vinny.nightfly.components.recipe.RecipeService;
import no.vinny.nightfly.components.task.*;
import no.vinny.nightfly.domain.Recipe;
import no.vinny.nightfly.domain.batch.*;
import no.vinny.nightfly.domain.tap.TapStatus;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Slf4j
@Service
public class BatchServiceImpl implements BatchService {

    private final static String REDIS_PREFIX = "Batch";
    private final BatchRepository batchRepository;
    private final RecipeService recipeService;
    private final TaskService taskService;

    public BatchServiceImpl(BatchRepository batchRepository, RecipeService recipeService, TaskService taskService) {
        this.batchRepository = batchRepository;
        this.recipeService = recipeService;
        this.taskService = taskService;
    }

    @Override
    @Cacheable(cacheNames = {"batch"}, key = "#root.methodName + '[' + #id + ']'")
    public Optional<Batch> get(Long id) {
        return batchRepository.findById(id);
    }

    @Override
    public Optional<BatchUnit> getBatchUnit(Long batchUnitId) {
        return batchRepository.getBatchUnit(batchUnitId);
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
        if (brewfatherId == null) {
            return Optional.empty();
        }
        return batchRepository.findByBrewfatherId(brewfatherId);
    }

    @Override
    public Batch create(Batch batch) {
        Optional<Batch> existing = getByBrewfatherId(batch.getBrewfatherId());
        if (existing.isPresent()) {
            return existing.get();
        }
        Batch newBatch = batchRepository.insert(batch);

        List<Task> tasks = createTasks(newBatch, TaskOrigin.MANUAL);
        if (!tasks.isEmpty()) {
            log.info("Creating {} tasks for batch {}", tasks.size(), batch.getId());
            taskService.create(tasks);
        }

        return newBatch;
    }

    // TODO: Return pagedlist
    @Override
    @Cacheable(cacheNames = {"batches"}, key = "#root.methodName + '[]'")
    public List<Batch> getAll() {
        return batchRepository.findAll();
    }

    @Override
    public List<Batch> filterBy(Long recipeId, Long tapId) {
        return batchRepository.getBatchesBy(recipeId,tapId);
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
            throw new NotFoundException("Batch not found");
        }
        batchRepository.update(mergeNonNull(batch, existingBatch.get()));
        return get(batch.getId()).get();
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
        return batch.getId() != null ? update(batch) : create(batch);
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

    @Override
    public BatchUnit update(BatchUnitDTO batchUnit) {
        if (batchUnit.getBatchId() == null) {
            log.error("Update rejected because batch unit id is null: {}", batchUnit);
            throw new IllegalArgumentException("Update rejected because batch unit id is null");
        }
        batchRepository.update(batchUnit);
        return batchRepository.getBatchUnit(batchUnit.getId()).orElseThrow(() -> new NotFoundException("Batch unit not found " + batchUnit.getId()));
    }

    @Override
    public List<BatchUnitDTO> findAllBy(Set<Long> batchIds, VolumeStatus volumeStatus, Set<TapStatus> excludeTapStatus) {
       return getAll().stream()
               .filter(batch -> ignore(batchIds) || batchIds.contains(batch.getId()))
               .flatMap(this::toDTO)
               .filter(bu -> volumeStatus == null || bu.getVolumeStatus() == volumeStatus)
               .filter(bu -> ignore(excludeTapStatus) || !excludeTapStatus.contains(bu.getTapStatus()))
               .collect(toList());
    }

    @Override
    public Optional<BatchUnitDTO> getBatchUnitById(Long batchUnitId) {
        if (batchUnitId == null) return Optional.empty();
        return batchRepository.getBatchAndBatchUnit(batchUnitId)
                .map(this::toSingle);

    }

    @Override
    public Optional<SyncEntity<Batch>> getLastImportedEntity() {
        return batchRepository.getLastImportedEntity();
    }

    @Override
    public int importBatch(String batch) {
        return batchRepository.syncBatch(batch);
    }

    @Override
    public List<SyncEntity<Batch>> findUnsynced() {
        return batchRepository.findUnsynced();
    }

    @Override
    @Transactional
    public void syncBatches() {
        // fetch all updates since last sync
        // but keep only latest version of each
        List<SyncEntity<Batch>> unsyncedBatches = batchRepository.findUnsynced();
        List<Batch> lastVersionOfEachBatch = unsyncedBatches.stream()
                .collect(Collectors.groupingBy(SyncEntity::brewfatherId))
                .values().stream()
                .map(batches -> batches.stream().max(Comparator.comparing(SyncEntity::updated)).orElse(null))
                .filter(Objects::nonNull)
                .map(SyncEntity::entity)
                .collect(Collectors.toUnmodifiableList());
        List<String> recipeBrewfatherIds = lastVersionOfEachBatch.stream()
                .map(Batch::getRecipe)
                .filter(Objects::nonNull)
                .map(Recipe::getBrewfatherId)
                .filter(Objects::nonNull)
                .collect(toUnmodifiableList());
        Map<String, Recipe> recipeByBrewfatherId = recipeService.findAllByBrewfatherIds(recipeBrewfatherIds).stream().collect(toMap(Recipe::getBrewfatherId, Function.identity()));

        Map<String, Batch> existingBatches = batchRepository.findAllByBrewfatherIds(lastVersionOfEachBatch.stream().map(Batch::getBrewfatherId).filter(Objects::nonNull).collect(Collectors.toUnmodifiableList())).stream()
                .collect(toMap(Batch::getBrewfatherId, Function.identity()));
        List<Batch> updatedBatches = lastVersionOfEachBatch.stream()
                .filter(batch -> existingBatches.containsKey(batch.getBrewfatherId()))
                .collect(Collectors.toUnmodifiableList());
        List<Batch> newBatches = lastVersionOfEachBatch.stream()
                .filter(batch -> !existingBatches.containsKey(batch.getBrewfatherId()))
                .collect(Collectors.toUnmodifiableList());

        // update existing batches
        updatedBatches.stream()
                .filter(updatedBatch -> hasChanges(existingBatches.get(updatedBatch.getBrewfatherId()), updatedBatch))
                .map(updatedBatch -> applyRecipe(updatedBatch, recipeByBrewfatherId))
                .map(updatedBatch -> applyUpdatesFrom(existingBatches.get(updatedBatch.getBrewfatherId()), updatedBatch))
                .forEach(updatedBatch -> {
                    log.info("[SYNC/{}] Updating existing batch {}", updatedBatch.getId(), updatedBatch);
                    batchRepository.update(updatedBatch);
                });
        // add new batches
        newBatches.stream()
                .map(batch -> applyRecipe(batch, recipeByBrewfatherId))
                .map(batchRepository::insert)
                .peek(batch -> log.info("[SYNC/{}] New batch {}", batch.getId(), batch))
                .map(this::createImportTasks)
                .flatMap(Collection::stream)
                .peek(task -> log.info("New task {}", task))
                .forEach(taskService::create);

        // mark everything as synced
        batchRepository.markAsSynced(unsyncedBatches.stream().map(SyncEntity::id).collect(toUnmodifiableList()));
    }

    private List<Task> createImportTasks(Batch batch) {
        return createTasks(batch, TaskOrigin.IMPORT);
    }
    private List<Task> createTasks(Batch batch, TaskOrigin origin) {
        List<Task> batchTasks = new ArrayList<>();
        if (batch.getBatchUnits() == null) {
            batchTasks.add(createTask(batch, origin, TaskType.NOT_PACKAGED));
        }
        // more tasks to come..
        return batchTasks;
    }

    private Task createTask(Batch batch, TaskOrigin origin, TaskType taskType) {
        return Task.builder()
                .brewfatherId(batch.getBrewfatherId())
                .entityId(batch.getId())
                .entity(taskEntityFrom(batch))
                .origin(origin)
                .task(taskType)
                .build();
    }

    private TaskEntity taskEntityFrom(Object entity) {
        return switch(entity) {
            case Batch batch -> TaskEntity.BATCH;
            case Keg keg     -> TaskEntity.KEG;
            default          -> throw new IllegalArgumentException("Unsupported TaskEntity type: " + entity.getClass());
        };
    }

    private Batch applyRecipe(Batch batch, Map<String, Recipe> recipes) {
        String brewfatherId = Optional.ofNullable(batch.getRecipe()).map(Recipe::getBrewfatherId).orElse(null);
        if (brewfatherId == null) {
            return batch;
        }
        if (!recipes.containsKey(brewfatherId)) {
            return batch;
        }
        return batch.toBuilder()
                .recipe(recipes.get(brewfatherId))
                .build();
    }

    private boolean hasChanges(Batch existing, Batch updatedBatch) {
        if (updatedBatch == null) {
            return false;
        }

        if (!existing.getName().equals(updatedBatch.getName())) {
            return true;
        }
        if (existing.getStatus() != updatedBatch.getStatus()) {
            return true;
        }

        return false;
    }

    private Batch applyUpdatesFrom(Batch existing, Batch updatedBatch) {
        if (updatedBatch == null) {
            return existing;
        }
        return existing.toBuilder()
                .name(updatedBatch.getName())
                .status(updatedBatch.getStatus())
                .build();
    }

    private boolean ignore(Set set) {
        return set == null || set.isEmpty();
    }

    private Stream<BatchUnitDTO> toDTO(Batch batch) {
        if (batch.getBatchUnits() == null || batch.getBatchUnits().isEmpty()) {
            return Stream.of();
        }
        return batch.getBatchUnits().stream()
                .map(bu -> BatchUnitDTO.builder()
                        .id(bu.getId())
                        .batchId(batch.getId())
                        .brewfatherId(batch.getBrewfatherId())
                        .name(batch.getName())
                        .tapStatus(bu.getTapStatus())
                        .packagingType(bu.getPackagingType())
                        .volumeStatus(bu.getVolumeStatus())
                        .keg(bu.getKeg())
                        .build());
    }
    private BatchUnitDTO toSingle(Batch batch) {
        if (batch.getBatchUnits() == null || batch.getBatchUnits().isEmpty()) {
            return null;
        }
        return batch.getBatchUnits().stream()
                .map(bu -> BatchUnitDTO.builder()
                        .id(bu.getId())
                        .batchId(batch.getId())
                        .brewfatherId(batch.getBrewfatherId())
                        .name(batch.getName())
                        .tapStatus(bu.getTapStatus())
                        .packagingType(bu.getPackagingType())
                        .volumeStatus(bu.getVolumeStatus())
                        .keg(bu.getKeg())
                        .build())
                .findAny()
                .orElse(null);
    }
    private void stall() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
