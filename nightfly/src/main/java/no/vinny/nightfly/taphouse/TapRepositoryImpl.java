package no.vinny.nightfly.taphouse;

import no.vinny.nightfly.batch.SQLTemplater;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TapRepositoryImpl implements TapRepository{

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String TAP_QUERY = SQLTemplater.tapQuery();
    private final TapRowMapper mapper;

    public TapRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = new TapRowMapper();
    }

    @Override
    public List<Tap> findAll() {
        return jdbcTemplate.query(TAP_QUERY, mapper);
    }
}
