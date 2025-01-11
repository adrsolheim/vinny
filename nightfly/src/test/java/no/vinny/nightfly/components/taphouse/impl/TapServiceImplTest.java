package no.vinny.nightfly.components.taphouse.impl;

import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.batch.domain.Batch;
import no.vinny.nightfly.components.batch.domain.Packaging;
import no.vinny.nightfly.components.taphouse.TapRepository;
import no.vinny.nightfly.components.taphouse.TapService;
import no.vinny.nightfly.components.taphouse.domain.Tap;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static no.vinny.nightfly.components.batch.domain.BatchStatus.COMPLETED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TapServiceImplTest {

    TapService tapService;
    TapRepository tapRepository;
    BatchService batchService = mock(BatchService.class);
    Map<Long, Tap> taps;

    @BeforeEach
    void setup() {
        taps = taps();

        tapRepository = new TapRepository() {
            @Override
            public Tap find(Long tap) {
                return taps.get(tap);
            }

            @Override
            public List<Tap> findAll() {
                return taps.values().stream().toList();
            }

            @Override
            public int update(Tap tap) {
                return 0;
            }
        };
        when(batchService.get(anyLong())).thenAnswer(a -> batchesMap().get(a.getArgument(0)));
        tapService = new TapServiceImpl(tapRepository, mock(BatchService.class));
    }
    @Test
    void find() {
        Optional<Tap> connectedTap = tapService.find(3L);

        assertTrue(connectedTap.isPresent());
    }

    @Test
    void findAll() {
        List<Tap> all = tapService.findAll();

        assertEquals(4, all.size());
    }

    @Test
    void findActive() {
        List<Tap> active = tapService.findActive();

        assertEquals(3, active.size());
        assertTrue(active.contains(new Tap(1L, true, batchesList().get(0))));
        assertTrue(active.contains(new Tap(2L, true, batchesList().get(1))));
        assertTrue(active.contains(new Tap(3L, true, batchesList().get(2))));
    }

    @Test
    void connectBatch() {
    }

    @Test
    void update() {
    }

    private Map<Long, Tap> taps() {
        return Map.of(
                1L, new Tap(1L, true, batchesList().get(0)),
                2L, new Tap(2L, true, batchesList().get(1)),
                3L, new Tap(3L, true, batchesList().get(2)),
                4L, new Tap(4L, false, null)
        );
    }

    private Map<Long, Batch> batchesMap() {
        return batchesList().stream().collect(Collectors.toMap(b -> b.getId(), Function.identity()));
    }
    private List<Batch> batchesList() {
        AtomicLong i = new AtomicLong(0);
        List<Batch> batches = Stream.of(
                                    "13574ef0d58b50fab38ec841efe39df4",
                                    "5eb63bbbe01eeed093cb22bb8f5acdc3",
                                    "b96b878ad72f56709dbb5628e1cea18d",
                                    "16e63f4d464ccd3c6014adad3dec89d5")
                            .map(bid -> Batch.builder()
                                    .id(i.getAndIncrement())
                                    .brewfatherId(bid)
                                    .name("Pilsner")
                                    .status(COMPLETED)
                                    .recipe(null)
                                    .build())
                            .collect(Collectors.toList());
        return batches;
    }
}