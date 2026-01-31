package no.vinny.nightfly.components.taphouse.impl;

import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.taphouse.TapRepository;
import no.vinny.nightfly.components.taphouse.api.ConnectBatchRequest;
import no.vinny.nightfly.domain.batch.BatchUnitDTO;
import no.vinny.nightfly.domain.batch.PackagingType;
import no.vinny.nightfly.domain.batch.VolumeStatus;
import no.vinny.nightfly.domain.tap.TapDTO;
import no.vinny.nightfly.domain.tap.TapStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TapServiceImplTest {

    @Mock
    TapRepository tapRepository;

    @Mock
    BatchService batchService;

    @InjectMocks
    TapServiceImpl tapService;

    @Captor
    ArgumentCaptor<BatchUnitDTO> batchUnitCaptor;

    @Captor
    ArgumentCaptor<TapDTO> tapCaptor;

    @Test
    void connectBatch_success_setsConnectedAndUpdates() {
        TapDTO tap = new TapDTO(1L, true, null);
        BatchUnitDTO unit = BatchUnitDTO.builder()
                .id(10L)
                .packagingType(PackagingType.KEG)
                .volumeStatus(VolumeStatus.NOT_EMPTY)
                .tapStatus(TapStatus.WAITING)
                .build();

        when(tapRepository.find(1L)).thenReturn(Optional.of(tap));
        when(batchService.getBatchUnitById(10L)).thenReturn(Optional.of(unit));

        TapDTO result = tapService.connectBatch(new ConnectBatchRequest(1L, 10L, false));

        assertThat(result.getId()).isEqualTo(1L);

        verify(batchService).update(batchUnitCaptor.capture());
        BatchUnitDTO updated = batchUnitCaptor.getValue();
        assertThat(updated.getTapStatus()).isEqualTo(TapStatus.CONNECTED);

        verify(tapRepository).update(tapCaptor.capture());
        TapDTO updatedTap = tapCaptor.getValue();
        assertThat(updatedTap.getBatchUnit().getId()).isEqualTo(10L);
    }

    @Test
    void connectBatch_disconnectsOldBatch_whenTapHasExistingBatch() {
        BatchUnitDTO newUnit = BatchUnitDTO.builder()
                .id(10L)
                .packagingType(PackagingType.KEG)
                .volumeStatus(VolumeStatus.NOT_EMPTY)
                .tapStatus(TapStatus.WAITING)
                .build();

        BatchUnitDTO oldUnit = BatchUnitDTO.builder()
                .id(2L)
                .packagingType(PackagingType.KEG)
                .volumeStatus(VolumeStatus.NOT_EMPTY)
                .tapStatus(TapStatus.SERVING)
                .build();
        TapDTO tap = new TapDTO(1L, true, oldUnit);

        when(tapRepository.find(1L)).thenReturn(Optional.of(tap));
        when(batchService.getBatchUnitById(10L)).thenReturn(Optional.of(newUnit));
        when(batchService.getBatchUnitById(2L)).thenReturn(Optional.of(oldUnit));

        tapService.connectBatch(new ConnectBatchRequest(1L, 10L, false));

        verify(batchService, times(2)).update(batchUnitCaptor.capture());
        List<BatchUnitDTO> captured = batchUnitCaptor.getAllValues();
        assertThat(captured).hasSize(2);
        assertThat(captured.get(0).getTapStatus()).isEqualTo(TapStatus.DISCONNECTED);
        assertThat(captured.get(0).getVolumeStatus()).isEqualTo(VolumeStatus.NOT_EMPTY);
        assertThat(captured.get(1).getTapStatus()).isEqualTo(TapStatus.CONNECTED);
    }

    @Test
    void connectBatch_throws_whenPackagingNotKeg() {
        TapDTO tap = new TapDTO(1L, true, null);
        BatchUnitDTO unit = BatchUnitDTO.builder()
                .id(11L)
                .packagingType(PackagingType.BOTTLE)
                .volumeStatus(VolumeStatus.NOT_EMPTY)
                .tapStatus(TapStatus.WAITING)
                .build();

        when(tapRepository.find(1L)).thenReturn(Optional.of(tap));
        when(batchService.getBatchUnitById(11L)).thenReturn(Optional.of(unit));

        assertThatThrownBy(() -> tapService.connectBatch(new ConnectBatchRequest(1L, 11L, false)))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Cannot connect");
    }

    @Test
    void connectBatch_throws_whenVolumeEmpty() {
        TapDTO tap = new TapDTO(1L, true, null);
        BatchUnitDTO unit = BatchUnitDTO.builder()
                .id(12L)
                .packagingType(PackagingType.KEG)
                .volumeStatus(VolumeStatus.EMPTY)
                .tapStatus(TapStatus.WAITING)
                .build();

        when(tapRepository.find(1L)).thenReturn(Optional.of(tap));
        when(batchService.getBatchUnitById(12L)).thenReturn(Optional.of(unit));

        assertThatThrownBy(() -> tapService.connectBatch(new ConnectBatchRequest(1L, 12L, false)))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Cannot connect empty keg");
    }

    @Test
    void connectBatch_throws_whenBatchAlreadyConnected() {
        TapDTO tap = new TapDTO(1L, true, null);
        BatchUnitDTO unit = BatchUnitDTO.builder()
                .id(13L)
                .packagingType(PackagingType.KEG)
                .volumeStatus(VolumeStatus.NOT_EMPTY)
                .tapStatus(TapStatus.CONNECTED)
                .build();

        when(tapRepository.find(1L)).thenReturn(Optional.of(tap));
        when(batchService.getBatchUnitById(13L)).thenReturn(Optional.of(unit));

        assertThatThrownBy(() -> tapService.connectBatch(new ConnectBatchRequest(1L, 13L, false)))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("already connected");
    }

    @Test
    void find_returnsTapDTO_whenPresent() {
        when(tapRepository.find(3L)).thenReturn(Optional.of(new TapDTO(3L, true, null)));

        Optional<TapDTO> connectedTap = tapService.find(3L);

        assertThat(connectedTap).isPresent();
        assertThat(connectedTap.get().getId()).isEqualTo(3L);
    }

    @Test
    void findAll_returnsAllTaps() {
        TapDTO t1 = new TapDTO(1L, true, null);
        TapDTO t2 = new TapDTO(2L, true, null);
        TapDTO t3 = new TapDTO(3L, true, null);
        TapDTO t4 = new TapDTO(4L, false, null);

        when(tapRepository.findAll()).thenReturn(Arrays.asList(t1, t2, t3, t4));

        List<TapDTO> all = tapService.findAll();

        assertThat(all).hasSize(4);
    }

    @Test
    void findActive_filtersByBatchOrActiveFlag() {
        var b1 = BatchUnitDTO.builder().id(1L).batchId(10L).build();
        var b2 = BatchUnitDTO.builder().id(2L).batchId(11L).build();
        var b3 = BatchUnitDTO.builder().id(3L).batchId(12L).build();

        TapDTO t1 = new TapDTO(1L, true, b1);
        TapDTO t2 = new TapDTO(2L, true, b2);
        TapDTO t3 = new TapDTO(3L, true, b3);
        TapDTO t4 = new TapDTO(4L, false, null);

        when(tapRepository.findAll()).thenReturn(Arrays.asList(t1, t2, t3, t4));

        List<TapDTO> active = tapService.findActive();

        assertThat(active).hasSize(3);
        assertThat(active).anyMatch(t -> t.getId().equals(1L) && t.getBatchUnit().getBatchId().equals(10L));
        assertThat(active).anyMatch(t -> t.getId().equals(2L) && t.getBatchUnit().getBatchId().equals(11L));
        assertThat(active).anyMatch(t -> t.getId().equals(3L) && t.getBatchUnit().getBatchId().equals(12L));
    }

    @Test
    void connectBatch_withOldBatchEmpty_true_disconnectsOldAndConnectsNew() {
        BatchUnitDTO newUnit = BatchUnitDTO.builder()
            .id(10L)
            .packagingType(PackagingType.KEG)
            .volumeStatus(VolumeStatus.NOT_EMPTY)
            .tapStatus(TapStatus.WAITING)
            .build();

        BatchUnitDTO oldUnit = BatchUnitDTO.builder()
            .id(2L)
            .packagingType(PackagingType.KEG)
            .volumeStatus(VolumeStatus.NOT_EMPTY)
            .tapStatus(TapStatus.SERVING)
            .build();
        TapDTO tap = new TapDTO(1L, true, oldUnit);

        when(tapRepository.find(1L)).thenReturn(Optional.of(tap));
        when(batchService.getBatchUnitById(10L)).thenReturn(Optional.of(newUnit));
        when(batchService.getBatchUnitById(2L)).thenReturn(Optional.of(oldUnit));

        tapService.connectBatch(new ConnectBatchRequest(1L, 10L, true));

        verify(batchService, times(2)).update(batchUnitCaptor.capture());
        List<BatchUnitDTO> captured = batchUnitCaptor.getAllValues();
        assertThat(captured).hasSize(2);
        assertThat(captured.get(0).getTapStatus()).isEqualTo(TapStatus.DISCONNECTED);
        assertThat(captured.get(0).getVolumeStatus()).isEqualTo(VolumeStatus.EMPTY);
        assertThat(captured.get(1).getTapStatus()).isEqualTo(TapStatus.CONNECTED);
    }
}