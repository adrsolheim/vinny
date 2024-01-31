package no.vinny.nightfly.components.taphouse.impl;

import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.batch.domain.Batch;
import no.vinny.nightfly.components.batch.domain.BatchStatus;
import no.vinny.nightfly.components.taphouse.TapRepository;
import no.vinny.nightfly.components.taphouse.TapService;
import no.vinny.nightfly.components.taphouse.domain.Tap;
import no.vinny.nightfly.util.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

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
                .filter(tap -> tap.getBatch() != null)
                .collect(Collectors.toList());
    }

    @Override
    public Tap connectBatch(Long tap, Long batchId) {
        Tap taphandle = find(tap).orElseThrow(() -> new ResourceNotFoundException("Tap not found " + tap));
        Batch batch = Optional.of(batchService.get(batchId))
                .get()
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found: " + batchId));
        if (taphandle.getBatch() != null) {
            Batch oldBatch = taphandle.getBatch();
            oldBatch.setStatus(BatchStatus.ARCHIVED);
            batchService.update(oldBatch);
        }
        taphandle.setBatch(batch);
        batchService.update(batch);
        update(taphandle);

        return taphandle;
    }

    public void update(Tap tap) {
        tapRepository.update(tap);
    }
}
