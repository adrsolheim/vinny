package no.vinny.nightfly.components.batch.impl;

import no.vinny.nightfly.components.batch.BatchRepository;
import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.batch.domain.Batch;
import no.vinny.nightfly.components.batch.domain.BatchStatus;
import no.vinny.nightfly.components.batch.domain.Mapper;
import no.vinny.nightfly.components.batch.impl.BatchServiceImpl;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.vinny.nightfly.components.batch.domain.BatchStatus.COMPLETED;
import static no.vinny.nightfly.components.batch.domain.BatchStatus.FERMENTING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BatchServiceImplTest {

    BatchService batchService;
    BatchRepository batchRepository;
    List<Batch> batchesById;
    Map<String, Batch> batchesByBrewfatherId;

    @BeforeEach
    void setup() {
        batchesById = batchesList();
        batchesByBrewfatherId = batchesMap();

        batchRepository = new BatchRepository() {
            @Override
            public int insert(Batch batch) {
                batch.setId(Long.valueOf(batchesById.size()));
                batchesById.add(batch);
                batchesByBrewfatherId.put(batch.getBrewfatherId(), batch);
                return 1;
            }

            @Override
            public int delete(Long id) {
                return 0;
            }

            @Override
            public void update(Batch batch) {
                if (batch.getId() == null) {
                    throw new IllegalArgumentException("Batch id must be present in order to find and update batch");
                }
                if (batch.getId() >= 0 && batch.getId() < batchesList().size()) {
                    batchesById.set(batch.getId().intValue(), batch);
                    batchesByBrewfatherId.put(batch.getBrewfatherId(), batch);
                }
            }

            @Override
            public Long count() {
                return Long.valueOf(batchesById.size());
            }

            @Override
            public Optional<Batch> findById(Long id) {
                if (id < 0 || id >= batchesList().size()) {
                    return Optional.empty();
                }
                return Optional.of(batchesById.get(id.intValue()));
            }

            @Override
            public Optional<Batch> findByBrewfatherId(String id) {
                if (batchesByBrewfatherId.containsKey(id)) {
                    return Optional.of(batchesByBrewfatherId.get(id));
                }
                return Optional.empty();
            }

            @Override
            public List<Batch> findAll() {
                return batchesById.stream().collect(Collectors.toList());
            }

            @Override
            public List<Batch> findByTapStatus(TapStatus status) {
                return null;
            }
        };
        RedisTemplate<String, Batch> redisTemplate = mock(RedisTemplate.class);
        when(redisTemplate.opsForValue()).thenReturn(mock(ValueOperations.class));
        when(redisTemplate.opsForValue().get(anyString())).thenReturn(null);
        batchService = new BatchServiceImpl(batchRepository, redisTemplate);
    }

    @Test
    void upsert_update_batch() {
        Batch expected = Batch.builder().id(1L).name("Updoot").brewfatherId("5eb63bbbe01eeed093cb22bb8f5acdc3").status(COMPLETED).build();
        Batch update   = Batch.builder().id(1L).name("Updoot").brewfatherId("5eb63bbbe01eeed093cb22bb8f5acdc3").build();

        Batch result = batchService.upsert(update);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void upsert_add_new_batch() {
        Batch expected = Batch.builder().id(4L).name("New Batch").brewfatherId("NEW63bbbe01eeed093cb22bb8f5acdc3").status(FERMENTING).build();
        Batch insert   = Batch.builder().name("New Batch").brewfatherId("NEW63bbbe01eeed093cb22bb8f5acdc3").status(FERMENTING).build();

        Batch result = batchService.upsert(insert);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void upsert_one_update_one_insert() {
        Batch expectedInsert = Batch.builder().id(4L).name("New Batch").brewfatherId("NEW63bbbe01eeed093cb22bb8f5acdc3").status(FERMENTING).build();
        Batch insert         = Batch.builder().name("New Batch").brewfatherId("NEW63bbbe01eeed093cb22bb8f5acdc3").status(FERMENTING).build();
        Batch update         = Batch.builder().id(1L).name("Updoot").brewfatherId("5eb63bbbe01eeed093cb22bb8f5acdc3").build();
        Batch expectedUpdate = Batch.builder().id(1L).name("Updoot").brewfatherId("5eb63bbbe01eeed093cb22bb8f5acdc3").status(COMPLETED).build();

        Batch insertResult = batchService.upsert(insert);
        Batch updateResult = batchService.upsert(update);
        Long count = batchService.count();

        Assertions.assertEquals(expectedInsert, insertResult);
        Assertions.assertEquals(expectedUpdate, updateResult);
        Assertions.assertEquals(5L, count);
    }

    private List<Batch> batchesList() {
        AtomicLong i = new AtomicLong(0);
        return Stream.of(
                        "13574ef0d58b50fab38ec841efe39df4",
                        "5eb63bbbe01eeed093cb22bb8f5acdc3",
                        "b96b878ad72f56709dbb5628e1cea18d",
                        "16e63f4d464ccd3c6014adad3dec89d5")
                .map(bid -> Batch.builder()
                        .id(i.getAndIncrement())
                        .brewfatherId(bid)
                        .name("TestBeer")
                        .status(COMPLETED)
                        .build())
                .collect(Collectors.toList());
    }

    private Optional<Batch> findByBrewfatherId(String brewfatherId) {
        return Optional.of(batchesMap().computeIfAbsent(brewfatherId, k -> null));
    }
    private Map<String, Batch> batchesMap() {
        return batchesList().stream().collect(Collectors.toMap(Batch::getBrewfatherId, Function.identity()));
    }
}