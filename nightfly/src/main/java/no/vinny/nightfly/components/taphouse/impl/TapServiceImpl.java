package no.vinny.nightfly.components.taphouse.impl;

import jakarta.persistence.EntityNotFoundException;
import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.batch.domain.Batch;
import no.vinny.nightfly.components.batch.domain.BatchStatus;
import no.vinny.nightfly.components.batch.domain.BatchUnit;
import no.vinny.nightfly.components.batch.domain.BatchUnitDTO;
import no.vinny.nightfly.components.taphouse.TapRepository;
import no.vinny.nightfly.components.taphouse.TapService;
import no.vinny.nightfly.components.taphouse.domain.Tap;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;
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
    public Optional<Tap> find(Long tap) {
        return Optional.ofNullable(tapRepository.find(tap));
    }

    @Override
    public List<Tap> findAll() {
        return tapRepository.findAll();
    }

    @Override
    public List<Tap> findActive() {
        return findAll().stream()
                .filter(tap -> tap.getBatchUnit() != null || tap.isActive())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Tap connectBatch(Long tap, Long batchUnitId) {
        Tap taphandle = find(tap).orElseThrow(() -> new ResourceNotFoundException("Tap not found " + tap));
        BatchUnitDTO batchUnit = batchService.getBatchUnitById(batchUnitId).orElseThrow(() -> new ResourceNotFoundException("BatchUnit not found: " + batchUnitId));
        if (taphandle.getBatchUnit() != null) {
            BatchUnit oldBatchUnit = batchService.getBatchUnit(taphandle.getBatchUnit().getId()).orElseThrow(() -> new ResourceNotFoundException("BatchUnit not found: " + batchUnitId));
            oldBatchUnit.setTapStatus(TapStatus.DISCONNECTED);
            batchService.update(oldBatchUnit);
        }
        taphandle.setBatchUnit(batchUnit);
        // TODO: keep record if previous tap? batchService.update(batchUnit);
        update(taphandle);

        return taphandle;
    }


    public void update(Tap tap) {
        tapRepository.update(tap);
    }
}
