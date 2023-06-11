package no.vinny.nightfly.batch.impl;

import no.vinny.nightfly.batch.Batch;
import no.vinny.nightfly.batch.BatchDTO;
import no.vinny.nightfly.batch.BatchRowMapper;
import no.vinny.nightfly.batch.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import reactor.core.publisher.Flux;

import java.util.List;

public class BatchRepositoryBlocking {

    private static final String BATCH_COLUMNS = "id, brewfather_id, name, status";
    private static final String SELECT_BATCH = "SELECT " + BATCH_COLUMNS + " FROM batch";
    private static final String INSERT_BATCH = "INSERT INTO " + BATCH_COLUMNS + " VALUES (:id, :brewfatherId, :name, :status)";
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final R2dbcEntityTemplate dbTemplate;

    @Autowired
    public BatchRepositoryBlocking(NamedParameterJdbcTemplate jdbcTemplate, R2dbcEntityTemplate dbTemplate) {
       this.jdbcTemplate = jdbcTemplate;
       this.dbTemplate = dbTemplate;
    }

    public int insert(BatchDTO batch) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", null);
        params.addValue("brewfatherId", batch.getBrewfatherId());
        params.addValue("name", batch.getName());
        params.addValue("status", batch.getStatus().getValue());
        String sql = INSERT_BATCH;
        return jdbcTemplate.update(sql, params);
    }

    public int delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sql = "DELETE FROM batch WHERE id=:id";
        return jdbcTemplate.update(sql, params);
    }

    public Flux<Batch> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sql = SELECT_BATCH + " WHERE id=" + id;
        return jdbcTemplate.query(sql, params, new BatchRowMapper());
    }

    public Flux<Batch> findByBrewfatherId(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sql = SELECT_BATCH + " WHERE brewfather_id=:id";
        return jdbcTemplate.query(sql, params, new BatchRowMapper());
    }

    public Flux<Batch> findAll() {
        String sql = SELECT_BATCH;
        return jdbcTemplate.query(sql, new BatchRowMapper());
    }
}
