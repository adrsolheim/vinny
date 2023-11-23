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
                rs.getLong("b_id"),
                rs.getString("b_brewfather_id"),
                rs.getString("b_name"),
                rs.getObject("b_status") != null ? BatchStatus.valueOf(rs.getString("b_status").toUpperCase()) : null,
                rs.getObject("b_tap_status") == null ? null : TapStatus.valueOf(rs.getString("b_tap_status")),
                rs.getObject("b_packaging") == null ? null : Packaging.valueOf(rs.getString("b_packaging")),
                mapRecipe(rs),
                rs.getObject("b_tap") == null ? null : rs.getLong("b_tap")
        );
    }

    private RecipeDTO mapRecipe(ResultSet rs) throws SQLException {
        if (!columnExist("recipe", rs) || rs.getObject("b_recipe") == null) {
            return null;
        }
        return RecipeDTO.builder()
                .id(rs.getLong("r_id"))
                .brewfatherId(rs.getString("r_brewfather_id"))
                .name(rs.getString("r_name"))
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
