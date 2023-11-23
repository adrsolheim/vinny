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
        return new Tap(rs.getObject("t_id", Long.class), mapBatch(rs));
    }

    private Batch mapBatch(ResultSet rs) throws SQLException {
        if (rs.getObject("t_batch") == null) {
            return null;
        }
        return new Batch(
                rs.getLong("b_id"),
                rs.getString("b_brewfather_id"),
                rs.getString("b_name"),
                rs.getObject("b_status") != null ? BatchStatus.valueOf(rs.getString("b_status").toUpperCase()) : null,
                rs.getObject("b_tap_status") == null ? null : TapStatus.valueOf(rs.getString("b_tap_status")),
                rs.getObject("b_packaging") == null ? null : Packaging.valueOf(rs.getString("b_packaging")),
                null,
                rs.getObject("b_tap") == null ? null : rs.getLong("b_tap")
        );
    }
}
