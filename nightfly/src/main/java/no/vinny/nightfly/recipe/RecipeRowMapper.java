package no.vinny.nightfly.recipe;

import no.vinny.nightfly.recipe.domain.RecipeDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeRowMapper implements RowMapper<RecipeDTO> {
    @Override
    public RecipeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RecipeDTO.builder()
                .id(rs.getLong("r_id"))
                .brewfatherId(rs.getString("r_brewfather_id"))
                .name(rs.getString("r_name"))
                .build();
    }
}
