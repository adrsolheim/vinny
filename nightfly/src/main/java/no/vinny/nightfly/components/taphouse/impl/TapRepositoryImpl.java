package no.vinny.nightfly.components.taphouse.impl;

import no.vinny.nightfly.components.batch.domain.BatchUnit;
import no.vinny.nightfly.components.taphouse.TapRepository;
import no.vinny.nightfly.components.taphouse.TapRowMapper;
import no.vinny.nightfly.components.taphouse.domain.Tap;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TapRepositoryImpl implements TapRepository {

    private final String TAP_COLUMNS = "t.id t_id, t.active t_active";
    private final String BATCH_UNIT_COLUMNS = "bu.id bu_id, bu.batch bu_batch_id, bu.tap_status bu_tap_status, bu.packaging bu_packaging, bu.volume_status bu_volume_status, bu.keg bu_keg";
    private final String KEG_COLUMNS = "k.id k_id, k.capacity k_capacity, k.brand k_brand, k.serial_number k_serial_number, k.purchase_condition k_purchase_condition, k.note k_note";

    private final String SELECT_TAP = "SELECT "
            + TAP_COLUMNS + ", "
            + BATCH_UNIT_COLUMNS + ", "
            + KEG_COLUMNS + " "
            + "FROM tap t LEFT JOIN batch_unit bu ON bu.id = t.batch_unit LEFT JOIN keg k ON bu.keg = keg.id";
    private final String UPDATE_TAP = "UPDATE tap SET active = :active, batch_unit = :batchUnit";

    private final TapRowMapper mapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TapRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = new TapRowMapper();
    }

    @Override
    public Tap find(Long tap) {
        String sql = SELECT_TAP + " WHERE t.id = :tap";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(Map.of("tap", tap)), mapper);
    }

    @Override
    public List<Tap> findAll() {
        return jdbcTemplate.query(SELECT_TAP, mapper);
    }

    @Override
    public int update(Tap tap) {
        String sql = UPDATE_TAP + " WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of("id", tap.getId(), "active", tap.isActive(), "batch", Optional.ofNullable(tap.getBatchUnit()).map(BatchUnit::getBatchId).orElse(null)));
        return jdbcTemplate.update(sql, params);
    }
}
