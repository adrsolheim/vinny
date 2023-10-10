package no.vinny.nightfly.batch.impl;

import no.vinny.nightfly.batch.Batch;
import no.vinny.nightfly.batch.BatchDTO;
import no.vinny.nightfly.batch.BatchRepository;
import no.vinny.nightfly.batch.BatchRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Optional;

public class BatchRepositoryImpl implements BatchRepository {

    private static final String BATCH_COLUMNS = "id, brewfather_id, name, status";
    private static final String SELECT_BATCH = "SELECT " + BATCH_COLUMNS + " FROM batch";
    private static final String INSERT_BATCH = "INSERT INTO " + BATCH_COLUMNS + " VALUES (:id, :brewfatherId, :name, :status)";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BatchRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
       this.jdbcTemplate = jdbcTemplate;
    }

    public int insert(BatchDTO batch) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", null);
        params.addValue("brewfatherId", batch.getBrewfatherId());
        params.addValue("name", batch.getName());
        params.addValue("status", Batch.Status.fromValue(batch.getStatus()).getValue());
        return jdbcTemplate.update(INSERT_BATCH, params);
    }

    public int delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcTemplate.update("DELETE FROM batch WHERE id=:id", params);
    }

    public Optional<Batch> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return Optional.of(jdbcTemplate.queryForObject(SELECT_BATCH + " WHERE id=" + id, params, new BatchRowMapper()));
    }

    public Optional<Batch> findByBrewfatherId(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return Optional.of(jdbcTemplate.queryForObject(SELECT_BATCH + " WHERE brewfather_id=:id", params, new BatchRowMapper()));
    }

    public List<Batch> findAll() {
        return jdbcTemplate.query(SELECT_BATCH, new BatchRowMapper());
    }
}
