package no.vinny.nightfly.components.recipe.impl;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.recipe.RecipeRepository;
import no.vinny.nightfly.components.recipe.RecipeRowMapper;
import no.vinny.nightfly.domain.Recipe;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class RecipeRepositoryImpl implements RecipeRepository {

    private static final String SELECT_RECIPE = "SELECT r.id r_id, r.brewfather_id r_brewfather_id, r.name r_name FROM recipe r";
    private static final String INSERT_RECIPE = "INSERT INTO recipe (brewfather_id, name) VALUES (:brewfatherId, :name)";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RecipeRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insert(Recipe recipe) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("brewfatherId", recipe.getBrewfatherId());
        params.addValue("name", recipe.getName());
        return jdbcTemplate.update(INSERT_RECIPE, params);
    }

    @Override
    public int delete(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcTemplate.update("DELETE FROM recipe WHERE id=:id", params);
    }

    @Override
    public void update(Recipe recipe) {
        MapSqlParameterSource params = new MapSqlParameterSource(convertToMap(recipe));
        String sql = "UPDATE recipe SET "
                + "brewfather_id = :brewfatherId, "
                + "name = :name "
                + "WHERE id = :id";
        jdbcTemplate.update(sql, params);
    }

    @Override
    public Long count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM recipe", Map.of(), Long.class);
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<Recipe> result = jdbcTemplate.query(SELECT_RECIPE + " WHERE id=:id", params, new RecipeRowMapper());
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Optional<Recipe> findByBrewfatherId(String brewfatherId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("brewfatherId", brewfatherId);
        List<Recipe> resultList = jdbcTemplate.query(SELECT_RECIPE + " WHERE brewfather_id = :brewfatherId", params, new RecipeRowMapper());
        return resultList.size() == 1 ? Optional.of(resultList.get(0)) : Optional.empty();
    }

    @Override
    public List<Recipe> findAll() {
        return jdbcTemplate.query(SELECT_RECIPE, new RecipeRowMapper());
    }

    private Map<String, Object> convertToMap(Recipe recipe) {
        Map<String, Object> batchMap = new HashMap<>();
        batchMap.put("id", recipe.getId());
        batchMap.put("brewfatherId", recipe.getBrewfatherId());
        batchMap.put("name", recipe.getName());
        return batchMap;
    }
}
