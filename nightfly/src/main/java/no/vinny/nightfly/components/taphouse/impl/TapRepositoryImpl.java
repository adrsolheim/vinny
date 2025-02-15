package no.vinny.nightfly.components.taphouse.impl;

import no.vinny.nightfly.components.taphouse.TapRepository;
import no.vinny.nightfly.components.taphouse.TapRowMapper;
import no.vinny.nightfly.components.taphouse.domain.Tap;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TapRepositoryImpl implements TapRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String SELECT_TAP = "SELECT t.id t_id, t.active t_active, t.batch t_batch, b.id b_id, b.brewfather_id b_brewfather_id, b.name b_name, b.status b_status FROM tap t LEFT JOIN batch b on b.id = t.batch";
    private final String UPDATE_TAP = "UPDATE tap SET active = :active, batch = :batch";
    private final TapRowMapper mapper;

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
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of("id", tap.getId(), "active", tap.isActive(), "batch", tap.getBatch()));
        return jdbcTemplate.update(sql, params);
    }
}
