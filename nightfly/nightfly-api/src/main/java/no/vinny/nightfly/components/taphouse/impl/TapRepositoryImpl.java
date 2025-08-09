package no.vinny.nightfly.components.taphouse.impl;

import no.vinny.nightfly.components.taphouse.TapRepository;
import no.vinny.nightfly.components.taphouse.TapRowDTOMapper;
import no.vinny.nightfly.domain.tap.Tap;
import no.vinny.nightfly.domain.tap.TapDTO;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TapRepositoryImpl implements TapRepository {

    private final String TAP_COLUMNS = "t.id t_id, t.active t_active, t.batch_unit t_batch_unit";
    private final String BATCH_COLUMNS = "b.id b_id, b.brewfather_id b_brewfather_id, b.name b_name, b.status b_status, b.recipe b_recipe";
    private final String BATCH_UNIT_COLUMNS = "bu.id bu_id, bu.batch bu_batch, bu.tap_status bu_tap_status, bu.packaging bu_packaging, bu.volume_status bu_volume_status, bu.keg bu_keg";
    private final String KEG_COLUMNS = "k.id k_id, k.capacity k_capacity, k.brand k_brand, k.serial_number k_serial_number, k.purchase_condition k_purchase_condition, k.note k_note";

    private final String SELECT_TAP = "SELECT "
            + TAP_COLUMNS + ", "
            + BATCH_COLUMNS + ", "
            + BATCH_UNIT_COLUMNS + ", "
            + KEG_COLUMNS + " "
            + "FROM tap t LEFT JOIN batch_unit bu ON bu.id = t.batch_unit LEFT JOIN batch b ON b.id = bu.batch LEFT JOIN keg k ON bu.keg = k.id";
    private final String UPDATE_TAP = "UPDATE tap SET active = :active, batch_unit = :batchUnit";

    private final TapRowDTOMapper mapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TapRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = new TapRowDTOMapper();
    }

    @Override
    public Optional<TapDTO> find(Long tap) {
        String sql = SELECT_TAP + " WHERE t.id = :tap";
        List<TapDTO> result = jdbcTemplate.query(sql, new MapSqlParameterSource(Map.of("tap", tap)), mapper);
        return result.size() == 1 ? Optional.of(result.get(0)) : Optional.empty();
    }

    @Override
    public Optional<Tap> findById(Long id) {
        String sql = SELECT_TAP + " WHERE t.id = :tap";
        List<Tap> result = jdbcTemplate.query(sql, new MapSqlParameterSource(Map.of("tap", id)), new RowMapper<Tap>() {
            @Override
            public Tap mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Tap(rs.getObject("t_id", Long.class), rs.getBoolean("t_active"), rs.getObject("t_batch_unit", Long.class));
            }
        });
        return result.size() == 1 ? Optional.of(result.get(0)) : Optional.empty();
    }

    @Override
    public List<TapDTO> findAll() {
        return jdbcTemplate.query(SELECT_TAP, mapper);
    }

    @Override
    public int update(Tap tap) {
        String sql = UPDATE_TAP + " WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of("id", tap.getId(), "active", tap.isActive(), "batchUnit", Optional.ofNullable(tap.getBatchUnitId()).orElse(null)));
        return jdbcTemplate.update(sql, params);
    }
}
