package no.vinny.nightfly.batch.impl;

import no.vinny.nightfly.batch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository("BatchRepository")
public class BatchRepositoryImpl implements BatchRepository {

    private static final String BATCH_COLUMNS = "id, brewfather_id, name, status";
    private static final String SELECT_BATCH = "SELECT " + BATCH_COLUMNS + " FROM batch";
    private static final String INSERT_BATCH = "INSERT INTO batch (brewfather_id, name, status) VALUES (:brewfatherId, :name, :status)";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BatchRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
       this.jdbcTemplate = jdbcTemplate;
    }

    public int insert(BatchDTO batch) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("brewfatherId", batch.getBrewfatherId());
        params.addValue("name", batch.getName());
        params.addValue("status", BatchStatus.fromValue(batch.getStatus()).getValue());
        return jdbcTemplate.update(INSERT_BATCH, params);
    }

    public int delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcTemplate.update("DELETE FROM batch WHERE id=:id", params);
    }

    public void update(BatchDTO batch) {
        MapSqlParameterSource params = new MapSqlParameterSource(convertToMap(batch));
        params.addValue("id", batch.getId());
        params.addValue("brewfather_id", batch.getBrewfatherId());
        params.addValue("name", batch.getName());
        params.addValue("status", batch.getStatus() == null ? null : BatchStatus.fromValue(batch.getStatus()).getValue());
        String sql = "UPDATE batch SET "
                + "brewfather_id = :brewfather_id, "
                + "name = :name, "
                + "status = :status "
                + "WHERE id = :id";
        jdbcTemplate.update(sql, params);
    }

    public Long count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM batch", Map.of(), Long.class);
    }

    public Optional<Batch> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return Optional.of(jdbcTemplate.queryForObject(SELECT_BATCH + " WHERE id=" + id, params, new BatchRowMapper()));
    }

    public Optional<Batch> findByBrewfatherId(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<Batch> resultList = jdbcTemplate.query(SELECT_BATCH + " WHERE brewfather_id=:id", params, new BatchRowMapper());
        if (resultList.isEmpty()) {
            return Optional.empty();
        }
        if (resultList.size() > 1) {
            throw new RuntimeException("Found multiple batches of id " + id);
        }
        return Optional.of(resultList.get(0));
    }

    public List<Batch> findAll() {
        return jdbcTemplate.query(SELECT_BATCH, new BatchRowMapper());
    }

    private Map<String, Object> convertToMap(BatchDTO batch) {
        Map<String, Object> batchMap = new HashMap<>();
        batchMap.put("id", batch.getId());
        batchMap.put("brewfather_id", batch.getBrewfatherId());
        batchMap.put("name", batch.getName());
        batchMap.put("status", batch.getStatus());
        return batchMap;
    }
}
