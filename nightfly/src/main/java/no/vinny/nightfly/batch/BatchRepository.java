package no.vinny.nightfly.batch;

import java.util.List;
import java.util.Optional;

public interface BatchRepository {
    public int insert(BatchDTO batch);
    public int delete(Long id);
    public Optional<Batch> findById(Long id);
    public Optional<Batch> findByBrewfatherId(String id);
    public List<Batch> findAll();
}
