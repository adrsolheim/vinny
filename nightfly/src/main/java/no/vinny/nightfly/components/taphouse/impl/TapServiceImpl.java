package no.vinny.nightfly.components.taphouse.impl;

import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.batch.domain.Batch;
import no.vinny.nightfly.components.batch.domain.BatchDTO;
import no.vinny.nightfly.components.batch.domain.Mapper;
import no.vinny.nightfly.components.taphouse.TapRepository;
import no.vinny.nightfly.components.taphouse.TapService;
import no.vinny.nightfly.components.taphouse.domain.Tap;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;
import no.vinny.nightfly.util.exception.OperationFailedException;
import no.vinny.nightfly.util.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TapServiceImpl implements TapService {

    private final TapRepository tapRepository;
    private final BatchService batchService;
    private final Mapper.BatchToDTO batchToDTO;
    private final Mapper.ToBatch dtoToBatch;

    public TapServiceImpl(TapRepository tapRepository,
                          BatchService batchService,
                          Mapper.BatchToDTO batchToDTO,
                          Mapper.ToBatch dtoToBatch) {
        this.tapRepository = tapRepository;
        this.batchService = batchService;
        this.batchToDTO = batchToDTO;
        this.dtoToBatch = dtoToBatch;
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
                .filter(tap -> List.of(TapStatus.CONNECTED, TapStatus.SERVING).contains(tap.getBatch().getTapStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public Tap connectBatch(Long tap, Long batchId) {
        Tap taphandle = find(tap).orElseThrow(() -> new ResourceNotFoundException("Tap not found " + tap));
        BatchDTO batchDTO = Optional.of(batchService.get(batchId))
                .get()
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found: " + batchId));
        if (batchDTO.getTap() != null) {
            throw new OperationFailedException("Batch already connected to tap " + batchDTO.getTap());
        }
        if (taphandle.getBatch() != null) {
            BatchDTO oldBatch = batchToDTO.apply(taphandle.getBatch());
            oldBatch.setStatus("ARCHIVED");
            batchService.update(oldBatch);
        }
        batchDTO.setTap(tap);
        taphandle.setBatch(dtoToBatch.apply(batchDTO));
        batchService.update(batchDTO);
        update(taphandle);

        return taphandle;
    }

    public void update(Tap tap) {
        tapRepository.update(tap);
    }
}
