package no.vinny.nightfly.components.keg;

import no.vinny.nightfly.domain.batch.Keg;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KegRepository {

    private static final String KEG_COLUMNS = "id, capacity, brand, serial_number, purchase_condition, note";
    private static final String SELECT_KEG = "SELECT " + KEG_COLUMNS + " FROM keg";


    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final KegRowMapper kegRowMapper;

    public KegRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.kegRowMapper = new KegRowMapper();
    }

    public List<Keg> findAll() {
        return jdbcTemplate.query(SELECT_KEG, kegRowMapper);
    }
}
