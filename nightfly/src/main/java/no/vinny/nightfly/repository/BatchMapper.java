package no.vinny.nightfly.repository;

import no.vinny.nightfly.domain.data.Batch;
import no.vinny.nightfly.domain.data.BatchStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BatchMapper implements RowMapper<Batch> {

    @Override
    public Batch mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Batch(
                rs.getLong("id"),
                rs.getString("brewfather_id"),
                rs.getString("name"),
                BatchStatus.valueOf(rs.getString("status").toUpperCase())
        );

    }
}
