package no.vinny.nightfly.batch;

import no.vinny.nightfly.batch.domain.Batch;
import no.vinny.nightfly.batch.domain.BatchStatus;
import no.vinny.nightfly.batch.domain.Packaging;
import no.vinny.nightfly.recipe.domain.RecipeDTO;
import no.vinny.nightfly.taphouse.TapStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class BatchRowMapper implements RowMapper<Batch> {

    @Override
    public Batch mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Batch(
                rs.getLong("id"),
                rs.getString("brewfather_id"),
                rs.getString("name"),
                rs.getObject("status") != null ? BatchStatus.valueOf(rs.getString("status").toUpperCase()) : null,
                rs.getObject("tap_status") == null ? null : TapStatus.valueOf(rs.getString("tap_status")),
                rs.getObject("packaging") == null ? null : Packaging.valueOf(rs.getString("packaging")),
                mapRecipe(rs),
                rs.getObject("tap") == null ? null : rs.getLong("tap")
        );
    }

    private RecipeDTO mapRecipe(ResultSet rs) throws SQLException {
        if (!columnExist("recipe", rs) || rs.getObject("recipe") == null) {
            return null;
        }
        return RecipeDTO.builder()
                .id(rs.getLong("r.id"))
                .brewfatherId(rs.getString("r.brewfather_id"))
                .name(rs.getString("r.name"))
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
}
