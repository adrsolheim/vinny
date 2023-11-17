package no.vinny.nightfly.batch.impl;

import no.vinny.nightfly.batch.*;
import no.vinny.nightfly.batch.domain.Batch;
import no.vinny.nightfly.batch.domain.BatchDTO;
import no.vinny.nightfly.batch.domain.BatchStatus;
import no.vinny.nightfly.batch.domain.Packaging;
import no.vinny.nightfly.taphouse.TapStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BatchRepositoryImpl implements BatchRepository {

    private static final String SELECT_BATCH = SQLTemplater.batchQuery(true, true);
    private static final String SELECT_BATCH_ONLY = SQLTemplater.batchQuery(true, false);
    private static final String INSERT_BATCH = SQLTemplater.batchInsert();
    private static final String UPDATE_BATCH = SQLTemplater.batchUpdate();
    private static final String BATCH_COUNT = SQLTemplater.batchCount();
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
        params.addValue("tap_status", TapStatus.WAITING.name());
        params.addValue("recipe", batch.getRecipe() == null ? null : batch.getRecipe().getId());
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
        params.addValue("brewfatherId", batch.getBrewfatherId());
        params.addValue("name", batch.getName());
        params.addValue("status", batch.getStatus() == null ? null : BatchStatus.fromValue(batch.getStatus()).getValue());
        params.addValue("packaging", Optional.of(batch.getPackaging()).map(Packaging::name).orElse(null));
        params.addValue("recipe", batch.getRecipe() == null ? null : batch.getRecipe().getId());
        jdbcTemplate.update(UPDATE_BATCH, params);
    }

    public Long count() {
        return jdbcTemplate.queryForObject(BATCH_COUNT, Map.of(), Long.class);
    }

    public Optional<Batch> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return Optional.of(jdbcTemplate.queryForObject(SELECT_BATCH_ONLY + " WHERE b.id = :id", params, new BatchRowMapper()));
    }

    public Optional<Batch> findByBrewfatherId(String id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("brewfatherId", id);
        List<Batch> resultList = jdbcTemplate.query( SELECT_BATCH + " WHERE b.brewfather_id=:brewfatherId", params, new BatchRowMapper());
        if (resultList.isEmpty()) {
            return Optional.empty();
        }
        if (resultList.size() > 1) {
            throw new RuntimeException("Found multiple batches of id " + id);
        }
        return Optional.of(resultList.get(0));
    }

    public List<Batch> findAll() {
        return jdbcTemplate.query(SELECT_BATCH_ONLY, new BatchRowMapper());
    }

    @Override
    public List<Batch> findByTapStatus(TapStatus status) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tapStatus", status.name());
        return jdbcTemplate.query(SELECT_BATCH_ONLY + " WHERE b.tap_status = :tapStatus", params, new BatchRowMapper());
    }

    private Map<String, Object> convertToMap(BatchDTO batch) {
        Map<String, Object> batchMap = new HashMap<>();
        batchMap.put("id", batch.getId());
        batchMap.put("brewfatherId", batch.getBrewfatherId());
        batchMap.put("name", batch.getName());
        batchMap.put("status", batch.getStatus());
        batchMap.put("packaging", Optional.of(batch.getPackaging()).map(Packaging::name).orElse(null));
        batchMap.put("recipe", batch.getRecipe() == null ? null : batch.getRecipe().getId());
        return batchMap;
    }
}
