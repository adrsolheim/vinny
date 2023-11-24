package no.vinny.nightfly.components.taphouse.impl;

import no.vinny.nightfly.components.taphouse.TapRepository;
import no.vinny.nightfly.components.taphouse.TapService;
import no.vinny.nightfly.components.taphouse.domain.Tap;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TapServiceImpl implements TapService {

    private final TapRepository tapRepository;

    public TapServiceImpl(TapRepository tapRepository) {
        this.tapRepository = tapRepository;
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
