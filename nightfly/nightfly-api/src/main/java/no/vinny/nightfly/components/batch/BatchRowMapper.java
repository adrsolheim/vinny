package no.vinny.nightfly.components.batch;

import no.vinny.nightfly.components.common.time.Time;
import no.vinny.nightfly.domain.batch.*;
import no.vinny.nightfly.domain.tap.TapStatus;
import no.vinny.nightfly.domain.Recipe;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class BatchRowMapper implements ResultSetExtractor<List<Batch>> {

    private Recipe mapRecipe(ResultSet rs) throws SQLException {
        if (!columnExist("recipe", rs) || rs.getObject("b_recipe") == null) {
            return null;
        }
        return Recipe.builder()
                .id(rs.getLong("r_id"))
                .brewfatherId(rs.getString("r_brewfather_id"))
                .name(rs.getString("r_name"))
                .updated(Time.toOsloTime(rs.getTimestamp("r_updated")))
                .build();
    }

    private boolean columnExist(String column, ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        for (int i = 1; i < metaData.getColumnCount()+1; i++) {
            if (metaData.getColumnName(i).equalsIgnoreCase(column)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Batch> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Batch> batches = new HashMap<>();
        Map<Long, BatchUnit> batchUnits = new HashMap<>();

        while(rs.next()) {
            Long batchId = rs.getLong("b_id");
            Batch batch = batches.get(batchId);
            if (batch == null) {
                batch = Batch.builder()
                        .id(rs.getLong("b_id"))
                        .brewfatherId(rs.getString("b_brewfather_id"))
                        .name(rs.getString("b_name"))
                        .status(rs.getObject("b_status") != null ? BatchStatus.valueOf(rs.getString("b_status").toUpperCase()) : null)
                        .recipe(mapRecipe(rs))
                        .updated(Time.toOsloTime(rs.getTimestamp("b_updated")))
                        .build();
                batches.put(batchId, batch);
            }
            Long unitId = rs.getObject("bu_id", Long.class);
            BatchUnit batchUnit = batchUnits.get(unitId);
            if (unitId != null && batchUnit == null) {
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
                batchUnit = BatchUnit.builder()
                        .id(unitId)
                        .batchId(batch.getId())
                        .tapId(rs.getObject("bu_tap", Long.class))
                        .tapStatus(rs.getString("bu_tap_status") == null ? null : TapStatus.valueOf(rs.getString("bu_tap_status")))
                        .packagingType(rs.getString("bu_packaging") == null ? null : PackagingType.valueOf(rs.getString("bu_packaging")))
                        .volumeStatus(rs.getString("bu_volume_status") == null ? null : VolumeStatus.valueOf(rs.getString("bu_volume_status")))
                        .occupiesKeg(rs.getObject("ko_keg_id", Long.class) != null)
                        .keg(keg)
                        .build();
                batchUnits.put(unitId, batchUnit);
            }
        }
        batchUnits.values().stream().forEach(bu -> {
            Batch batch = batches.get(bu.getBatchId());
            if (batch.getBatchUnits() == null) {
                batch.setBatchUnits(new ArrayList<>());
            }
            batch.getBatchUnits().add(bu);
        });
        return batches.values().stream().collect(Collectors.toList());
    }
}
