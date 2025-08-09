package no.vinny.nightfly.components.batch.impl;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.batch.BatchRepository;
import no.vinny.nightfly.components.batch.BatchRowMapper;
import no.vinny.nightfly.components.batch.BatchUnitRowMapper;
import no.vinny.nightfly.domain.batch.Batch;
import no.vinny.nightfly.domain.batch.BatchUnit;
import no.vinny.nightfly.domain.tap.TapStatus;
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
    private static final String BATCH_UNIT_COLUMNS = "bu.id bu_id, bu.batch bu_batch, bu.tap_status bu_tap_status, bu.packaging bu_packaging, bu.volume_status bu_volume_status, bu.keg bu_keg";
    private static final String KEG_COLUMNS = "k.id k_id, k.capacity k_capacity, k.brand k_brand, k.serial_number k_serial_number, k.purchase_condition k_purchase_condition, k.note k_note";

    private static final String SELECT_BATCH = "SELECT "
            + BATCH_COLUMNS + ", "
            + RECIPE_COLUMNS + ", "
            + BATCH_UNIT_COLUMNS + ", "
            + KEG_COLUMNS
            + " FROM batch b INNER JOIN batch_unit bu on bu.batch = b.id "
            + " LEFT JOIN keg k ON k.id = bu.keg "
            + " LEFT JOIN recipe r on r.id = b.recipe ";
    private static final String SELECT_BATCH_ONLY = "SELECT " + BATCH_COLUMNS;

    private static final String INSERT_BATCH_UNIT = "INSERT INTO batch_unit (batch, tap_status, packaging, volume_status, keg) VALUES (:batch, :tapStatus, :packaging, :volumeStatus, :keg)";
    private static final String UPDATE_BATCH_UNIT = "UPDATE batch_unit SET batch = :batch, tap_status = :tapStatus, packaging = :packaging, volume_status = :volumeStatus, keg = :keg WHERE id = :id";

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
        int[] dbResult = jdbcTemplate.batchUpdate(INSERT_BATCH_UNIT, batchUnits.stream().map(this::toSqlParams).toArray(Map[]::new));
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
        MapSqlParameterSource params = new MapSqlParameterSource(toSqlParams(batch));
        jdbcTemplate.update(UPDATE_BATCH, params);
    }

    public void update(BatchUnit batchUnit) {
        MapSqlParameterSource params = toSqlParams(batchUnit);
        jdbcTemplate.update(UPDATE_BATCH_UNIT, params);
    }

    public Long count() {
        return jdbcTemplate.queryForObject(BATCH_COUNT, Map.of(), Long.class);
    }

    public Optional<Batch> findById(Long id) {
        stall();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            List<Batch> result = jdbcTemplate.query(SELECT_BATCH + " WHERE b.id = :id", params, new BatchRowMapper());
            return result.size() == 1 ? Optional.of(result.get(0)) : Optional.empty();
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

    @Override
    public Optional<BatchUnit> getBatchUnit(Long batchUnitId) {
        String sql = "SELECT " + BATCH_UNIT_COLUMNS + ", " + KEG_COLUMNS + " FROM batch_unit bu LEFT JOIN keg k on bu.keg = k.id WHERE bu.id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", batchUnitId);
        List<BatchUnit> result = jdbcTemplate.query(sql, params, new BatchUnitRowMapper());

        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Optional<Batch> getBatchAndBatchUnit(Long batchUnitId) {
        String sql = SELECT_BATCH + " WHERE bu.id = :batchUnitId";
        MapSqlParameterSource params = new MapSqlParameterSource("batchUnitId", batchUnitId);
        List<Batch> result = jdbcTemplate.query(sql, params, new BatchRowMapper());

        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }


    private Map<String, Object> toSqlParams(Batch batch) {
        Map<String, Object> batchMap = new HashMap<>();
        batchMap.put("id", batch.getId());
        batchMap.put("brewfatherId", batch.getBrewfatherId());
        batchMap.put("name", batch.getName());
        batchMap.put("status", batch.getStatus() == null ? null : batch.getStatus().name());
        batchMap.put("recipe", batch.getRecipe() == null ? null : batch.getRecipe().getId());
        return batchMap;
    }

    private MapSqlParameterSource toSqlParams(BatchUnit batchUnit) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", batchUnit.getId());
        params.addValue("batch",  batchUnit.getBatchId());
        params.addValue("tapStatus", batchUnit.getTapStatus() == null ? null : batchUnit.getTapStatus().name());
        params.addValue("packaging", batchUnit.getPackaging() == null ? null : batchUnit.getPackaging().name());
        params.addValue("volumeStatus", batchUnit.getVolumeStatus() == null ? null : batchUnit.getVolumeStatus().name());
        params.addValue("keg", batchUnit.getKeg() == null ? null : batchUnit.getKeg().getId());
        return params;
    }

    private void stall() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
