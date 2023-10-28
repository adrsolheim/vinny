package no.vinny.nightfly.batch;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BatchRowMapper implements RowMapper<Batch> {

    @Override
    public Batch mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Batch(
                rs.getLong("id"),
                rs.getString("brewfatherId"),
                rs.getString("name"),
                rs.getObject("status") != null ? BatchStatus.valueOf(rs.getString("status").toUpperCase()) : null
        );

    }
}
