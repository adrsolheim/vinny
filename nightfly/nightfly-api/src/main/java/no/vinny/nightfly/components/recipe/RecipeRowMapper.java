package no.vinny.nightfly.components.recipe;

import no.vinny.nightfly.domain.Recipe;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeRowMapper implements RowMapper<Recipe> {
    @Override
    public Recipe mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Recipe.builder()
                .id(rs.getLong("r_id"))
                .brewfatherId(rs.getString("r_brewfather_id"))
                .name(rs.getString("r_name"))
                .build();
    }
}
