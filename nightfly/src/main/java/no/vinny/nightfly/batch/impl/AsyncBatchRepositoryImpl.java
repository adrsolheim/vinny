package no.vinny.nightfly.batch.impl;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.Batch;
import no.vinny.nightfly.batch.BatchDTO;
import no.vinny.nightfly.batch.AsyncBatchRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@RequiredArgsConstructor
@Repository
@Slf4j
public class AsyncBatchRepositoryImpl implements AsyncBatchRepository {
   // TODO: replace DatabaseClient with R2dbcEntityTemplate?
   private final DatabaseClient databaseClient;

   public static final BiFunction<Row, RowMetadata, Batch> BATCH_MAPPER = (row, metadata) -> Batch.builder()
           .id(row.get("id", Long.class))
           .brewfatherId(row.get("brewfather_id", String.class))
           .name(row.get("name", String.class))
           .status(row.get("status", String.class) == null ? null : Batch.Status.valueOf(row.get("status", String.class)))
           .build();

   public static final BiFunction<Row, RowMetadata, Long> LONG_MAPPER = (row, metadata) -> (Long) row.get(0);

   @Override
   public Flux<Batch> findAll(Pageable pageable) {
      StringBuilder query = new StringBuilder();
      query.append("SELECT id, brewfather_id, name, status FROM batch ");
      query.append("LIMIT ").append(pageable.getPageSize());
      query.append("OFFSET ").append(pageable.getOffset());
      return databaseClient
              .sql(query.toString())
              .map(BATCH_MAPPER)
              .all();
   }

   @Override
   public Mono<Batch> findById(Long id) {
      return databaseClient
              .sql("SELECT id, brewfather_id, name, status FROM batch WHERE id = :id")
              .bind("id", id)
              .map(BATCH_MAPPER)
              .one();
   }

   @Override
   public Flux<Batch> findByBrewfatherId(String brewfatherId) {
      return databaseClient
              .sql("SELECT id, brewfather_id, name, status FROM batch WHERE brewfather_id = :brewfatherId")
              .bind("brewfatherId", brewfatherId)
              .map(BATCH_MAPPER)
              .all();
   }

   @Override
   public Mono<Long> count() {
      return databaseClient
              .sql("SELECT COUNT(id) as n FROM batch")
              .map(LONG_MAPPER)
              .one();
   }

   @Override
   public Mono<Long> deleteAll() {
      return databaseClient
              .sql("DELETE FROM batch")
              .fetch()
              .rowsUpdated();
   }

   @Override
   public Mono<Long> save(BatchDTO batch) {
      return databaseClient.sql("""
                INSERT INTO batch (brewfather_id, name, status) 
                VALUES (:brewfather_id, :name, :status)
                """)
              .bind("brewfather_id", batch.getBrewfatherId())
              .bind("name", batch.getName())
              .bind("status", Batch.Status.fromValue(batch.getStatus()).getValue().toUpperCase())
              .map((row, rowMetadata) -> row.get("id", Long.class))
              .first();
   }

   @Override
   public Mono<Batch> update(BatchDTO batch) {
      return databaseClient.sql("""
                UPDATE batch 
                SET brewfather_id = :brewfather_id, name = :name, status = :status 
                WHERE id = :id 
                """)
              .bind("id", batch.getId())
              .bind("brewfather_id", batch.getBrewfatherId())
              .bind("name", batch.getName())
              .bind("status", Batch.Status.fromValue(batch.getStatus()).getValue().toUpperCase())
              .map((row, rowMetadata) -> row.get("id", Long.class))
              .first()
              .flatMap(this::findById)
              .doOnNext(b -> log.info("Batch update: {}"));

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
}
