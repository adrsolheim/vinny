package no.vinny.nightfly.components.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.vinny.nightfly.components.common.time.Time;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class TaskRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    private static final String TASK_COLUMNS = "id, brewfather_id, handled, handled_by, entity_id, entity_type, origin, task";
    private static final String SELECT_TASK = "SELECT " + TASK_COLUMNS + " FROM task";
    private static final String INSERT_TASK = "INSERT INTO task (brewfather_id, handled, handled_by, entity_id, entity_type, origin, task) VALUES (:brewfatherId, :handled, :handledBy, :entityId, :entityType, :origin, :task)";

    public TaskRepository(NamedParameterJdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Task> findAll() {
        return jdbcTemplate.query(SELECT_TASK, new TaskRowMapper());
    }

    public List<Task> findAllUnhandled() {
        return jdbcTemplate.query(SELECT_TASK + " WHERE handled is null", new TaskRowMapper());
    }

    public List<Task> findBy(Long entityId, String brewfatherId, TaskType type, Boolean handled) {
        StringBuilder sql = new StringBuilder(SELECT_TASK + " WHERE 1=1 ");
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (entityId != null) {
            sql.append("AND entity_id = :entityId ");
            params.addValue("entityId", entityId);
        }
        if (brewfatherId != null) {
            sql.append("AND brewfather_id = :brewfatherId ");
            params.addValue("brewfatherId", brewfatherId);
        }
        if (type != null) {
            sql.append("AND type = :type ");
            params.addValue("type", type);
        }
        if (handled != null) {
            sql.append(handled ? "AND handled is not null " : "AND handled is null ");
        }

        return jdbcTemplate.query(sql.toString(), params, new TaskRowMapper());
    }

    public void insert(Task task) {
        jdbcTemplate.update(INSERT_TASK, toSqlParams(task));
    }

    public void markAsHandled(List<Long> ids, String handledBy) {
        if (ids.isEmpty()) {
            throw new IllegalArgumentException("ids cannot be empty when marking tasks as handled");
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
        params.addValue("handled", Time.now());
        params.addValue("handledBy", handledBy);

        String sql = "UPDATE task SET handled = :handled, handled_by = :handledBy WHERE id in (:ids)";
        jdbcTemplate.update(sql, params);
    }

    private MapSqlParameterSource toSqlParams(Task task) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("brewfatherId", task.getBrewfatherId());
        params.addValue("handled", task.getHandled());
        params.addValue("handledBy", task.getHandledBy());
        params.addValue("entityId", task.getEntityId());
        params.addValue("entityType", task.getEntity() == null ? null : task.getEntity().name());
        params.addValue("origin", task.getOrigin() == null ? null : task.getOrigin().name());
        params.addValue("task", task.getTask() == null ? null : task.getTask().name());

        return params;
    }
}
