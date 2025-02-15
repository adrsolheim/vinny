package no.vinny.nightfly.components.batch.impl;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.batch.BatchRepository;
import no.vinny.nightfly.components.batch.BatchRowMapper;
import no.vinny.nightfly.components.batch.domain.Batch;
import no.vinny.nightfly.components.batch.domain.BatchUnit;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class BatchRepositoryImpl implements BatchRepository {

    private static final String BATCH_COLUMNS = "b.id b_id, b.brewfather_id b_brewfather_id, b.name b_name, b.status b_status, b.recipe b_recipe";
    private static final String RECIPE_COLUMNS = "r.id r_id, r.brewfather_id r_brewfather_id, r.name r_name";

    private static final String SELECT_BATCH = "SELECT " + BATCH_COLUMNS + ", " + RECIPE_COLUMNS + " FROM batch b LEFT JOIN recipe r on r.id = b.recipe ";
    private static final String SELECT_BATCH_ONLY = "SELECT " + BATCH_COLUMNS;

    private static final String INSERT_BATCH_UNIT = "INSERT INTO batch_unit (batch, tap_status, packaging, volume_status, keg) VALUES (:batch, :tapStatus, :packaging, :volumeStatus, :keg)";
    private static final String INSERT_BATCH = "INSERT INTO batch (brewfather_id, name, status, recipe) VALUES (:brewfatherId, :name, :status, :recipe)";
    private static final String UPDATE_BATCH = "UPDATE batch SET brewfather_id = :brewfatherId, name = :name, status = :status, recipe = :recipe WHERE id = :id ";
    private static final String BATCH_COUNT = "SELECT COUNT(*) FROM batch";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BatchRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
       this.jdbcTemplate = jdbcTemplate;
    }

    public int insert(Batch batch) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("brewfatherId", batch.getBrewfatherId());
        params.addValue("name", batch.getName());
        params.addValue("status", batch.getStatus() == null ? null : batch.getStatus().name());
        //params.addValue("tapStatus", TapStatus.WAITING.name());
        params.addValue("recipe", batch.getRecipe() == null ? null : batch.getRecipe().getId());
        int insertBatchResult = jdbcTemplate.update(INSERT_BATCH, params);
        if (batch.getBatchUnits() != null) {
            insertAll(batch.getBatchUnits());
        }
        return insertBatchResult;
    }

    public int insertAll(List<BatchUnit> batchUnits) {
        if (batchUnits == null || batchUnits.isEmpty()) {
            return 0;
        }
        int[] dbResult = jdbcTemplate.batchUpdate(INSERT_BATCH_UNIT, batchUnits.stream().map(this::convertToMap).toArray(Map[]::new));
        int inserts = Arrays.stream(dbResult).reduce(0, (a, b) -> a + b);
        if (inserts < batchUnits.size()) {
            log.warn("{}/{} batch units inserted for batch {}", inserts, batchUnits.size(), batchUnits.get(0).getBatchId());
        }
        return inserts;
    }

    public int delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcTemplate.update("DELETE FROM batch WHERE id=:id", params);
    }

    public void update(Batch batch) {
        MapSqlParameterSource params = new MapSqlParameterSource(convertToMap(batch));
        params.addValue("id", batch.getId());
        params.addValue("brewfatherId", batch.getBrewfatherId());
        params.addValue("name", batch.getName());
        params.addValue("status", batch.getStatus() == null ? null : batch.getStatus().name());
        params.addValue("recipe", batch.getRecipe() == null ? null : batch.getRecipe().getId());
        jdbcTemplate.update(UPDATE_BATCH, params);
    }

    public Long count() {
        return jdbcTemplate.queryForObject(BATCH_COUNT, Map.of(), Long.class);
    }

    public Optional<Batch> findById(Long id) {
        stall();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BATCH + " WHERE b.id = :id", params, new BatchRowMapper()));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
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
        return jdbcTemplate.query(SELECT_BATCH, new BatchRowMapper());
    }

    @Override
    public List<Batch> findByTapStatus(TapStatus status) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("tapStatus", status.name());
        return jdbcTemplate.query(SELECT_BATCH_ONLY + " WHERE b.tap_status = :tapStatus", params, new BatchRowMapper());
    }

    @Override
    public List<BatchUnit> findBatchUnits(Set<Long> batchIds, Set<TapStatus> excludeTapStatus) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql = "SELECT r.name r_name, r.brewfather_id r_brewfather_id, b.id b_id, bu.id bu_id, bu.tap_status bu_tap_status, bu.volume_status bu_volume_status FROM recipe r INNER JOIN batch b on b.recipe = r.id INNER JOIN batch_unit bu ON bu.batch = b.id WHERE 1=1 ";
        if (!batchIds.isEmpty()) {
            sql += " AND id IN (:batchIds)";
            params.addValue("batchIds", batchIds);
        }
        if (!excludeTapStatus.isEmpty()) {
            sql += " AND tap_status NOT IN (:excludeTapStatus)";
            params.addValue("excludeTapStatus", excludeTapStatus);
        }
        //jdbcTemplate.query(sql, params, new BatchUnitRowMapper());
        return null;
    }

    private Map<String, Object> convertToMap(Batch batch) {
        Map<String, Object> batchMap = new HashMap<>();
        batchMap.put("id", batch.getId());
        batchMap.put("brewfatherId", batch.getBrewfatherId());
        batchMap.put("name", batch.getName());
        batchMap.put("status", batch.getStatus());
        batchMap.put("recipe", batch.getRecipe() == null ? null : batch.getRecipe().getId());
        return batchMap;
    }

    private Map<String, Object> convertToMap(BatchUnit batchUnit) {
        Map<String, Object> batchUnitMap = new HashMap<>();
        batchUnitMap.put("id", batchUnit.getId());
        batchUnitMap.put("batch",  batchUnit.getBatchId());
        batchUnitMap.put("tapStatus", batchUnit.getTapStatus() == null ? null : batchUnit.getTapStatus().name());
        batchUnitMap.put("packaging", batchUnit.getPackaging() == null ? null : batchUnit.getPackaging().name());
        batchUnitMap.put("volumeStatus", batchUnit.getVolumeStatus() == null ? null : batchUnit.getVolumeStatus().name());
        batchUnitMap.put("keg", batchUnit.getKeg() == null ? null : batchUnit.getKeg().getId());
        return batchUnitMap;
    }

    private void stall() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
