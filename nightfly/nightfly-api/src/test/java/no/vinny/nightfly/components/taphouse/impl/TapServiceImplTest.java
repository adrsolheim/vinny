package no.vinny.nightfly.components.taphouse.impl;

import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.domain.batch.*;
import no.vinny.nightfly.components.taphouse.TapRepository;
import no.vinny.nightfly.components.taphouse.TapService;
import no.vinny.nightfly.domain.tap.Tap;
import no.vinny.nightfly.domain.tap.TapStatus;
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

import static no.vinny.nightfly.domain.batch.BatchStatus.COMPLETED;
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
        assertTrue(active.stream().anyMatch(t -> t.getId().equals(1L) && t.getBatchUnit().getBatchId().equals(10L)));
        assertTrue(active.stream().anyMatch(t -> t.getId().equals(2L) && t.getBatchUnit().getBatchId().equals(11L)));
        assertTrue(active.stream().anyMatch(t -> t.getId().equals(3L) && t.getBatchUnit().getBatchId().equals(12L)));
    }

    @Test
    void connectBatch() {
    }

    @Test
    void update() {
    }

    private Map<Long, Tap> taps() {
        return Map.of(
                1L, new Tap(1L, true, batchUnitMap().get(1L)),
                2L, new Tap(2L, true, batchUnitMap().get(2L)),
                3L, new Tap(3L, true, batchUnitMap().get(3L)),
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

    /**
     *
     INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg)
     VALUES (1, 10, 'CONNECTED',    'KEG', 'NOT_EMPTY', 1);
     INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg)
     VALUES (2, 10, 'CONNECTED',    'KEG', 'NOT_EMPTY', 2);
     INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg)
     VALUES (3, 11, 'CONNECTED',    'KEG', 'NOT_EMPTY', 3);
     INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg)
     VALUES (4, 11, 'DISCONNECTED', 'KEG', 'EMPTY',     4);
     */

    private Map<Long, BatchUnitDTO> batchUnitMap() {
        Keg defaultKeg = Keg.builder().id(1L).capacity(19.5).build();
        return Map.of(
                1L, BatchUnitDTO.builder().id(1L).batchId(10L).tapStatus(TapStatus.CONNECTED).packaging(Packaging.KEG).volumeStatus(VolumeStatus.NOT_EMPTY).keg(defaultKeg).build(),
                2L, BatchUnitDTO.builder().id(2L).batchId(11L).tapStatus(TapStatus.CONNECTED).packaging(Packaging.KEG).volumeStatus(VolumeStatus.NOT_EMPTY).keg(defaultKeg).build(),
                3L, BatchUnitDTO.builder().id(3L).batchId(12L).tapStatus(TapStatus.CONNECTED).packaging(Packaging.KEG).volumeStatus(VolumeStatus.NOT_EMPTY).keg(defaultKeg).build()
        );
    }
}