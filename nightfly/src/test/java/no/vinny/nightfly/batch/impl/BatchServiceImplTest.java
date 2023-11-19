package no.vinny.nightfly.batch.impl;

import no.vinny.nightfly.batch.*;
import no.vinny.nightfly.batch.domain.Batch;
import no.vinny.nightfly.batch.domain.BatchDTO;
import no.vinny.nightfly.batch.domain.Mapper;
import no.vinny.nightfly.taphouse.TapStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


class BatchServiceImplTest {

    BatchService batchService;
    BatchRepository batchRepository;
    Mapper.ToBatch toBatch;
    Mapper.BatchToDTO batchToDTO;
    List<BatchDTO> batchesById;
    Map<String, BatchDTO> batchesByBrewfatherId;

    @BeforeEach
    void setup() {
        batchesById = batchesList();
        batchesByBrewfatherId = batchesMap();
        toBatch = new Mapper.ToBatch();
        batchToDTO = new Mapper.BatchToDTO();

        batchRepository = new BatchRepository() {
            @Override
            public int insert(BatchDTO batch) {
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
            public void update(BatchDTO batch) {
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
                return Optional.of(batchesById.get(id.intValue())).map(toBatch);
            }

            @Override
            public Optional<Batch> findByBrewfatherId(String id) {
                if (batchesByBrewfatherId.containsKey(id)) {
                    return Optional.of(batchesByBrewfatherId.get(id)).map(toBatch);
                }
                return Optional.empty();
            }

            @Override
            public List<Batch> findAll() {
                return batchesById.stream().map(toBatch).collect(Collectors.toList());
            }

            @Override
            public List<Batch> findByTapStatus(TapStatus status) {
                return null;
            }
        };
        batchService = new BatchServiceImpl(batchRepository, batchToDTO);
    }

    @Test
    void upsert_update_batch() {
        BatchDTO expected = BatchDTO.builder().id(1L).name("Updoot").brewfatherId("5eb63bbbe01eeed093cb22bb8f5acdc3").status("Completed").build();
        BatchDTO update   = BatchDTO.builder().id(1L).name("Updoot").brewfatherId("5eb63bbbe01eeed093cb22bb8f5acdc3").build();

        BatchDTO result = batchService.upsert(update);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void upsert_add_new_batch() {
        BatchDTO expected = BatchDTO.builder().id(4L).name("New Batch").brewfatherId("NEW63bbbe01eeed093cb22bb8f5acdc3").status("Fermenting").build();
        BatchDTO insert   = BatchDTO.builder().name("New Batch").brewfatherId("NEW63bbbe01eeed093cb22bb8f5acdc3").status("Fermenting").build();

        BatchDTO result = batchService.upsert(insert);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void upsert_one_update_one_insert() {
        BatchDTO expectedInsert = BatchDTO.builder().id(4L).name("New Batch").brewfatherId("NEW63bbbe01eeed093cb22bb8f5acdc3").status("Fermenting").build();
        BatchDTO insert         = BatchDTO.builder().name("New Batch").brewfatherId("NEW63bbbe01eeed093cb22bb8f5acdc3").status("Fermenting").build();
        BatchDTO update         = BatchDTO.builder().id(1L).name("Updoot").brewfatherId("5eb63bbbe01eeed093cb22bb8f5acdc3").build();
        BatchDTO expectedUpdate = BatchDTO.builder().id(1L).name("Updoot").brewfatherId("5eb63bbbe01eeed093cb22bb8f5acdc3").status("Completed").build();

        BatchDTO insertResult = batchService.upsert(insert);
        BatchDTO updateResult = batchService.upsert(update);
        Long count = batchService.count();

        Assertions.assertEquals(expectedInsert, insertResult);
        Assertions.assertEquals(expectedUpdate, updateResult);
        Assertions.assertEquals(5L, count);
    }

    private List<BatchDTO> batchesList() {
        List<String> bids = List.of(
                "13574ef0d58b50fab38ec841efe39df4",
                "5eb63bbbe01eeed093cb22bb8f5acdc3",
                "b96b878ad72f56709dbb5628e1cea18d",
                "16e63f4d464ccd3c6014adad3dec89d5");
        List<BatchDTO> batches = new ArrayList<>();
        int i = 0;
        for(String b : bids) {
            batches.add(BatchDTO.builder()
                    .id(Long.valueOf(i))
                    .brewfatherId(bids.get(i))
                    .name("TestBeer")
                    .status("Completed")
                    .build());
            i++;
        }
        return batches;
    }

    private Optional<BatchDTO> findByBrewfatherId(String brewfatherId) {
        return Optional.of(batchesMap().computeIfAbsent(brewfatherId, k -> null));
    }
    private Map<String, BatchDTO> batchesMap() {
        return batchesList().stream().collect(Collectors.toMap(BatchDTO::getBrewfatherId, Function.identity()));
    }
}