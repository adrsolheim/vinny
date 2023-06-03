package no.vinny.nightfly.batch;

import org.springframework.data.repository.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface BatchRepository extends Repository<Batch, Long> {
    public int insert(BatchDTO batch);
    public int delete(Long id);
    public Flux<Batch> findById(Long id);
    public Flux<Batch> findByBrewfatherId(String id);
    public Flux<Batch> findAll();
}
