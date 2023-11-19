package no.vinny.nightfly.taphouse;

import no.vinny.nightfly.batch.BatchRepository;
import no.vinny.nightfly.batch.domain.Batch;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TapServiceImpl implements TapService {

    private final BatchRepository batchRepository;

    public TapServiceImpl(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    private List<Batch> findBatchesOnTap() {
        return batchRepository.findByTapStatus(TapStatus.SERVING);
    }

    public List<Tap> findOnTap() {
        return findBatchesOnTap().stream()
                .map(this::from)
                .sorted()
                .collect(Collectors.toList());
    }

    private Tap from(Batch batch) {
        return new Tap(batch.getTap(), batch.getName());
    }



}
