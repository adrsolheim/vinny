package no.vinny.nightfly.batch;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BatchRowMapper implements RowMapper<Batch> {

    @Override
    public Batch mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Batch(
                rs.getLong("id"),
                rs.getObject("brewfather_id") != null ? rs.getString("brewfather_id") : null,
                rs.getObject("name") != null ? rs.getString("name") : null,
                rs.getObject("status") != null ? Batch.Status.valueOf(rs.getString("status").toUpperCase()) : null
        );

    }
}
