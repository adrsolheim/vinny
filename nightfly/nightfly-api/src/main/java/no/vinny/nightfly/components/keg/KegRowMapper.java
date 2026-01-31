package no.vinny.nightfly.components.keg;

import no.vinny.nightfly.domain.batch.Keg;
import no.vinny.nightfly.domain.batch.PurchaseCondition;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KegRowMapper implements RowMapper<Keg> {
    @Override
    public Keg mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Keg.builder()
                .id(rs.getObject("id", Long.class))
                .capacity(rs.getObject("capacity", Double.class))
                .brand(rs.getString("brand"))
                .serialNumber(rs.getString("serial_number"))
                .purchaseCondition(rs.getString("purchase_condition") == null ? null : PurchaseCondition.valueOf(rs.getString("purchase_condition")))
                .note(rs.getString("note"))
                .build();
    }
}
