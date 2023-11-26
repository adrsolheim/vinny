package no.vinny.nightfly.components.taphouse.impl;

import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.batch.domain.BatchDTO;
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

    public TapServiceImpl(TapRepository tapRepository, BatchService batchService) {
        this.tapRepository = tapRepository;
        this.batchService = batchService;
    }

    @Override
    public Optional<Tap> find(Long tap) {
        return Optional.ofNullable(tapRepository.find(tap));
    }

    public List<Tap> findAll() {
        return tapRepository.findAll();
    }

    public List<Tap> findActive() {
        return findAll().stream()
                .filter(tap -> tap.getBatch() != null)
                .filter(tap -> List.of(TapStatus.CONNECTED, TapStatus.SERVING).contains(tap.getBatch().getTapStatus()))
                .collect(Collectors.toList());
    }
}
