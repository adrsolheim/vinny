package no.vinny.nightfly.batch;

import no.vinny.nightfly.batch.domain.Batch;
import no.vinny.nightfly.batch.domain.BatchDTO;

import java.util.List;
import java.util.Optional;

public interface BatchRepository {
    int insert(BatchDTO batch);

    int delete(Long id);

    void update(BatchDTO batch);

    Long count();

    Optional<Batch> findById(Long id);

    Optional<Batch> findByBrewfatherId(String id);

    List<Batch> findAll();
}