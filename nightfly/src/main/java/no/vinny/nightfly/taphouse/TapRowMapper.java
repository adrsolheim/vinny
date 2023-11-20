package no.vinny.nightfly.taphouse;

import no.vinny.nightfly.batch.domain.Batch;
import no.vinny.nightfly.batch.domain.BatchStatus;
import no.vinny.nightfly.batch.domain.Packaging;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TapRowMapper implements RowMapper<Tap> {
    @Override
    public Tap mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Tap(rs.getLong("t.id"), mapBatch(rs));
    }

    private Batch mapBatch(ResultSet rs) throws SQLException {
        if (rs.getObject("t.batch") == null) {
            return null;
        }
        return new Batch(
                rs.getLong("id"),
                rs.getString("brewfather_id"),
                rs.getString("name"),
                rs.getObject("status") != null ? BatchStatus.valueOf(rs.getString("status").toUpperCase()) : null,
                rs.getObject("tap_status") == null ? null : TapStatus.valueOf(rs.getString("tap_status")),
                rs.getObject("packaging") == null ? null : Packaging.valueOf(rs.getString("packaging")),
                null,
                rs.getObject("tap") == null ? null : rs.getLong("tap")
        );
    }
}
