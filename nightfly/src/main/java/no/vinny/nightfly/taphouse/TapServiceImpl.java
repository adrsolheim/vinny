package no.vinny.nightfly.taphouse;

import no.vinny.nightfly.batch.BatchRepository;
import no.vinny.nightfly.batch.domain.Batch;
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
}
