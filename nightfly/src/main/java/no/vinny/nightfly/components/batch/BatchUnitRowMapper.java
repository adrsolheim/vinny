package no.vinny.nightfly.components.batch;

import no.vinny.nightfly.components.batch.domain.Batch;
import no.vinny.nightfly.components.batch.domain.BatchStatus;
import no.vinny.nightfly.components.batch.domain.BatchUnit;
import no.vinny.nightfly.components.recipe.domain.Recipe;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class BatchUnitRowMapper extends RowMapper<BatchUnitDTO> {
    @Override
    public BatchUnitDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BatchUnitDTO(
                rs.getLong("b_id"),
                rs.getString("b_brewfather_id"),
                rs.getString("b_name"),
                rs.getObject("b_status") != null ? BatchStatus.valueOf(rs.getString("b_status").toUpperCase()) : null,
                mapRecipe(rs),
                null
        );
    }

    private Recipe mapRecipe(ResultSet rs) throws SQLException {
        if (!columnExist("recipe", rs) || rs.getObject("b_recipe") == null) {
            return null;
        }
        return Recipe.builder()
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
