package no.vinny.nightfly.batch;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface BatchRepository extends Repository<Batch, Long> {
    public Mono<Batch> save(BatchDTO batch);
    public Mono<Void> deleteById(Long id);
    public Flux<Batch> findById(Long id);
    @Query(value = """
            SELECT id, brewfather_id, name, status
            FROM batch
            WHERE brewfather_id = :brewfatherId
            """)
    public Mono<Batch> findByBrewfatherId(String brewfatherId);
    @Query(value = """
            SELECT id, brewfather_id, name, status
            FROM batch
            """)
    public Flux<Batch> findAll();
    public Mono<Long> count();
    @Query(value = """
            SELECT status
            FROM batch
            """)
    public Flux<Batch.Status> statuses();
}
