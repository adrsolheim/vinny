package no.vinny.nightfly.batch.impl;

import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import no.vinny.nightfly.batch.Batch;
import no.vinny.nightfly.batch.BatchDTO;
import no.vinny.nightfly.batch.BatchObjectMapper;
import no.vinny.nightfly.batch.BatchRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.BiFunction;

@Repository
public class R2dbcBatchRepository implements BatchRepository {

    private DatabaseClient databaseClient;

    private BiFunction<Row, RowMetadata, Batch> BATCH_ROW_MAPPER = (row, rowMetadata) -> {
        return Batch.builder()
                .id(row.get("id", Long.class))
                .brewfatherId(row.get("brewfather_id", String.class))
                .name(row.get("name", String.class))
                .status(Batch.Status.valueOf(row.get("status", String.class)))
                .build();
    };
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.databaseClient = DatabaseClient.create(connectionFactory);
    }
    @Override
    public Mono<Long> save(BatchDTO batch) {
        return databaseClient.sql("""
                INSERT INTO batch (brewfather_id, name, status) 
                VALUES (:brewfather_id, :name, :status)
                """)
                .bind("brewfather_id", batch.getBrewfatherId())
                .bind("name", batch.getName())
                .bind("status", batch.getStatus().name())
                .map((row, rowMetadata) -> row.get("id", Long.class))
                .first();
    }

    @Override
    public Mono<Long> deleteById(Long id) {
        return databaseClient.sql("""
                DELETE FROM batch
                WHERE id = :id
                """)
                .bind("id", id)
                .fetch()
                .rowsUpdated();
    }

    @Override
    public Mono<Batch> findById(Long id) {
        return databaseClient.sql("""
            SELECT id, brewfather_id, name, status
            FROM batch
            WHERE id = :id
                """)
                .bind("id", id)
                .map(BATCH_ROW_MAPPER)
                .first();
    }

    @Override
    public Flux<Batch> findByBrewfatherId(String brewfatherId) {
        return databaseClient.sql("""
            SELECT id, brewfather_id, name, status
            FROM batch
            WHERE brewfather_id = :brewfatherId
                """)
                .bind("brewfatherId", brewfatherId)
                .map(BATCH_ROW_MAPPER)
                .all();
    }

    @Override
    public Flux<Batch> findAll() {
        return databaseClient.sql("""
            SELECT id, brewfather_id, name, status
            FROM batch
                """)
                .map(BATCH_ROW_MAPPER)
                .all();
    }

    @Override
    public Mono<Long> count() {
        return databaseClient.sql("""
            SELECT COUNT(id) as n
            FROM batch
                """)
                .map((row, rowMetadata) -> row.get("n", Long.class))
                .first();
    }
}
