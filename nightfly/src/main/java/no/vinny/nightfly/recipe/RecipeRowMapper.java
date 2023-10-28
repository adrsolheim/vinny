package no.vinny.nightfly.recipe;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeRowMapper implements RowMapper<RecipeDTO> {
    @Override
    public RecipeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RecipeDTO.builder()
                .id(rs.getLong("id"))
                .brewfatherId(rs.getString("brewfather_id"))
                .name(rs.getString("name"))
                .build();
    }
}
