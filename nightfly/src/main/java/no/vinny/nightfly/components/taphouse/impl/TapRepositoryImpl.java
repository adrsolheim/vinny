package no.vinny.nightfly.components.taphouse.impl;

import no.vinny.nightfly.components.SQLTemplater;
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
    private final String TAP_QUERY = SQLTemplater.tapQuery();
    private final TapRowMapper mapper;

    public TapRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = new TapRowMapper();
    }

    @Override
    public Tap find(Long tap) {
        return jdbcTemplate.queryForObject(TAP_QUERY + " WHERE id = :tap", new MapSqlParameterSource(Map.of("id", tap)), mapper);
    }

    @Override
    public List<Tap> findAll() {
        return jdbcTemplate.query(TAP_QUERY, mapper);
    }
}
