package no.vinny.nightfly.components.recipe.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.common.sync.SyncEntity;
import no.vinny.nightfly.components.common.time.Time;
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

    private static final String SELECT_RECIPE = "SELECT r.id r_id, r.brewfather_id r_brewfather_id, r.name r_name, r.updated r_updated FROM recipe r";
    private static final String INSERT_RECIPE = "INSERT INTO recipe (brewfather_id, name, updated) VALUES (:brewfatherId, :name, :updated)";
    private static final String SYNC_RECIPE = "INSERT INTO sync_recipe (brewfather_id, updated_epoch, entity) VALUES (JSON_VALUE(:entity, '$._id'), JSON_VALUE(:entity, '$._timestamp_ms'), :entity)";

    private static final String SELECT_LAST_SYNCED_ENTITY = "SELECT id, brewfather_id, updated_epoch, entity FROM sync_batch ORDER BY updated_epoch DESC LIMIT 1";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public RecipeRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public int insert(Recipe recipe) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("brewfatherId", recipe.getBrewfatherId());
        params.addValue("name", recipe.getName());
        params.addValue("updated", Time.now());
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
                + "name = :name, "
                + "updated = :updated "
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

    @Override
    public int importRecipe(String recipe) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("entity", recipe);
        return jdbcTemplate.update(SYNC_RECIPE, params);
    }

    @Override
    public Optional<SyncEntity<Recipe>> getLastImportedEntity() {
        List<SyncEntity<Recipe>> result = jdbcTemplate.query(SELECT_LAST_SYNCED_ENTITY, Map.of(), (rs, rowNum) -> new SyncEntity(rs.getObject("id", Long.class), rs.getString("brewfather_id"), rs.getObject("updated_epoch", Long.class), recipeFromJson(rs.getString("entity"))));
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    private Recipe recipeFromJson(String json) {
        if (json == null) {
            return null;
        }
        JsonNode root = null;
        try {
            root = objectMapper.readTree(json);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        return Recipe.builder()
                .name(root.path("name").asText())
                .brewfatherId(root.path("_id").asText())
                .build();
    }

    private Map<String, Object> convertToMap(Recipe recipe) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", recipe.getId());
        params.put("brewfatherId", recipe.getBrewfatherId());
        params.put("name", recipe.getName());
        params.put("updated", Time.now());
        return params;
    }
}
