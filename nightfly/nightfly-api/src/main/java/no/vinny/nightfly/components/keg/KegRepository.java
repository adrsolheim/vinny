package no.vinny.nightfly.components.keg;

import no.vinny.nightfly.domain.batch.Keg;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KegRepository {

    private static final String KEG_COLUMNS           = "k.id k_id, k.capacity k_capacity, k.brand k_brand, k.serial_number k_serial_number, k.purchase_condition k_purchase_condition, k.note k_note";
    private static final String KEG_OCCUPANCY_COLUMNS = "ko.keg_id ko_keg_id, ko.batch_unit_id ko_batch_unit_id, ko.batch_id ko_batch_id, ko.occupied_at ko_occupied_at";

    private static final String SELECT_KEG = "SELECT "
            + KEG_COLUMNS + ", "
            + KEG_OCCUPANCY_COLUMNS
            + " FROM keg k"
            + " INNER JOIN keg_occupancy ko ON ko.keg_id = k.id";



    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final KegRowMapper kegRowMapper;

    public KegRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.kegRowMapper = new KegRowMapper();
    }

    public List<Keg> findAll() {
        return jdbcTemplate.query(SELECT_KEG, kegRowMapper);
    }


    public List<Keg> findAvailable() {
        return jdbcTemplate.query(SELECT_KEG + " WHERE ko.batch_unit_id IS NULL", kegRowMapper);
    }
}
