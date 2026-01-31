package no.vinny.nightfly.components.taphouse;

import no.vinny.nightfly.domain.batch.*;
import no.vinny.nightfly.domain.tap.TapDTO;
import no.vinny.nightfly.domain.tap.TapStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TapRowDTOMapper implements RowMapper<TapDTO> {

    @Override
    public TapDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TapDTO(rs.getObject("t_id", Long.class), rs.getBoolean("t_active"), mapBatchUnit(rs));
    }

    private BatchUnitDTO mapBatchUnit(ResultSet rs) throws SQLException {
        Long batchUnitId = rs.getObject("bu_id", Long.class);
        if (batchUnitId == null) {
            return null;
        }
        Long kegId = rs.getObject("bu_keg", Long.class);
        Keg keg = kegId == null ? null
                : Keg.builder()
                .id(kegId)
                .capacity(rs.getObject("k_capacity", Double.class))
                .brand(rs.getString("k_brand"))
                .serialNumber(rs.getString("k_serial_number"))
                .purchaseCondition(rs.getString("k_purchase_condition") == null ? null : PurchaseCondition.valueOf(rs.getString("k_purchase_condition")))
                .note(rs.getString("k_note"))
                .build();
        return BatchUnitDTO.builder()
                .id(batchUnitId)
                .name(rs.getString("b_name"))
                .brewfatherId(rs.getString("b_brewfather_id"))
                .batchId(rs.getObject("bu_batch", Long.class))
                .tapStatus(rs.getString("bu_tap_status") == null ? null : TapStatus.valueOf(rs.getString("bu_tap_status")))
                .packagingType(rs.getString("bu_packaging") == null ? null : PackagingType.valueOf(rs.getString("bu_packaging")))
                .volumeStatus(rs.getString("bu_volume_status") == null ? null : VolumeStatus.valueOf(rs.getString("bu_volume_status")))
                .keg(keg)
                .build();
    }
}
