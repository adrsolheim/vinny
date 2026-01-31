package no.vinny.nightfly.components.keg;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.domain.batch.Keg;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KegService {

    private final KegRepository kegRepository;

    public KegService(KegRepository kegRepository) {
        this.kegRepository = kegRepository;
    }

    public List<Keg> findAll() {
        return kegRepository.findAll();
    }
}
