package no.vinny.nightfly.components.taphouse.impl;

import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.domain.batch.BatchUnit;
import no.vinny.nightfly.domain.batch.BatchUnitDTO;
import no.vinny.nightfly.components.taphouse.TapRepository;
import no.vinny.nightfly.components.taphouse.TapService;
import no.vinny.nightfly.domain.batch.Packaging;
import no.vinny.nightfly.domain.batch.VolumeStatus;
import no.vinny.nightfly.domain.tap.Tap;
import no.vinny.nightfly.domain.tap.TapDTO;
import no.vinny.nightfly.domain.tap.TapStatus;
import no.vinny.nightfly.util.exception.ApiException;
import no.vinny.nightfly.util.exception.ResourceNotFoundException;
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
        return Optional.ofNullable(tapRepository.find(tap));
    }

    @Override
    public Optional<Tap> findById(Long id) {
        return Optional.ofNullable(tapRepository.findById(id));
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
    public TapDTO connectBatch(Long tapId, Long batchUnitId) {
        Tap tap = tapRepository.findById(tapId);
        BatchUnit batchUnit = batchService.getBatchUnit(batchUnitId).orElseThrow(() -> new ResourceNotFoundException("Batch unit not found " + batchUnitId));
        if (canConnect(batchUnit)) {
            batchUnit.setTapStatus(TapStatus.CONNECTED);
        }
        if (tap.getBatchUnitId() != null) {
            BatchUnit oldBatchUnit = batchService.getBatchUnit(tap.getBatchUnitId()).orElseThrow(() -> new ResourceNotFoundException("BatchUnit not found: " + batchUnitId));
            oldBatchUnit.setTapStatus(TapStatus.DISCONNECTED);
            batchService.update(oldBatchUnit);
        }
        tap.setBatchUnitId(batchUnitId);
        update(tap);
        batchService.update(batchUnit);

        return find(tapId).orElseThrow(() -> new ResourceNotFoundException("Tap not found " + tap));
    }

    private boolean canConnect(BatchUnit batchUnit) {
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
    public void update(Tap tap) {
        tapRepository.update(tap);
    }
}
