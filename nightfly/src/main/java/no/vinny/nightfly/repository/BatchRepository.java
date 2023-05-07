package no.vinny.nightfly.repository;

import no.vinny.nightfly.domain.data.Batch;
import no.vinny.nightfly.domain.dto.BatchDTO;

import java.util.List;
import java.util.Optional;

public interface BatchRepository {
    public int insert(BatchDTO batch);
    public int delete(Long id);
    public Optional<Batch> findById(Long id);
    public Optional<Batch> findByBrewfatherId(String id);
    public List<Batch> findAll();
}
