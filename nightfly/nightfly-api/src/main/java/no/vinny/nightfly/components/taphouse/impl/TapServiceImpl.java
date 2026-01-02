package no.vinny.nightfly.components.taphouse.impl;

import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.taphouse.api.ConnectBatchRequest;
import no.vinny.nightfly.components.taphouse.TapRepository;
import no.vinny.nightfly.components.taphouse.TapService;
import no.vinny.nightfly.domain.batch.BatchUnitDTO;
import no.vinny.nightfly.domain.batch.Packaging;
import no.vinny.nightfly.domain.batch.VolumeStatus;
import no.vinny.nightfly.domain.tap.TapDTO;
import no.vinny.nightfly.domain.tap.TapStatus;
import no.vinny.nightfly.util.exception.ApiException;
import no.vinny.nightfly.components.common.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TapServiceImpl implements TapService {

    private final TapRepository tapRepository;
    private final BatchService batchService;

    public TapServiceImpl(TapRepository tapRepository, BatchService batchService) {
        this.tapRepository = tapRepository;
        this.batchService = batchService;
    }

    @Override
    public Optional<TapDTO> find(Long tap) {
        return tapRepository.find(tap);
    }

    @Override
    public List<TapDTO> findAll() {
        return tapRepository.findAll();
    }

    @Override
    public List<TapDTO> findActive() {
        return findAll().stream()
                .filter(tap -> tap.getBatchUnit() != null || tap.isActive())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public TapDTO connectBatch(ConnectBatchRequest request) {
        TapDTO tap = tapRepository.find(request.tapId()).orElseThrow(() -> new NotFoundException("Tap not found " + request.tapId()));
        BatchUnitDTO batchUnit = batchService.getBatchUnitById(request.batchUnitId()).orElseThrow(() -> new NotFoundException("Batch unit not found " + request.batchUnitId()));
        if (canConnect(batchUnit)) {
            batchUnit.setTapStatus(TapStatus.CONNECTED);
        }
        if (tap.getBatchUnit() != null) {
            BatchUnitDTO oldBatchUnit = batchService.getBatchUnitById(tap.getBatchUnit().getId()).orElseThrow(() -> new NotFoundException("BatchUnit not found: " + request.batchUnitId()));
            oldBatchUnit.setTapStatus(TapStatus.DISCONNECTED);
            oldBatchUnit.setVolumeStatus(request.oldBatchEmpty() ? VolumeStatus.EMPTY : oldBatchUnit.getVolumeStatus());
            batchService.update(oldBatchUnit);
        }
        tap.setBatchUnit(batchUnit);
        update(tap);
        batchService.update(batchUnit);

        return find(request.tapId()).orElseThrow(() -> new NotFoundException("Tap not found " + tap));
    }

    private boolean canConnect(BatchUnitDTO batchUnit) {
        if (batchUnit.getPackaging() != Packaging.KEG) {
            throw ApiException.conflict("Cannot connect " + batchUnit.getPackaging().name() + " to tap. Must be " + Packaging.KEG.name());
        }
        if (batchUnit.getVolumeStatus() == VolumeStatus.EMPTY) {
            throw ApiException.conflict("Cannot connect empty keg");
        }
        if (batchUnit.getTapStatus() == TapStatus.CONNECTED) {
            throw ApiException.conflict("Batch unit is already connected");
        }
        return true;
    }
    public void update(TapDTO tap) {
        tapRepository.update(tap);
    }
}
