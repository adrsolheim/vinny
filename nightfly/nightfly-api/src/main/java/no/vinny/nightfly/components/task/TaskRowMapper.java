package no.vinny.nightfly.components.task;

import no.vinny.nightfly.components.common.time.Time;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper<Task> {
    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Task.builder()
                .id(rs.getObject("id", Long.class))
                .brewfatherId(rs.getString("brewfather_id"))
                .handled(Time.toOsloTime(rs.getTimestamp("handled")))
                .handledBy(rs.getString("handled_by"))
                .entityId(rs.getObject("entity_id", Long.class))
                .entity(rs.getString("entity_type") == null ? null : TaskEntity.valueOf(rs.getString("entity_type")))
                .origin(rs.getString("origin") == null ? null : TaskOrigin.valueOf(rs.getString("origin")))
                .task(rs.getString("task") == null ? null : TaskType.valueOf(rs.getString("task")))
                .build();
    }
}
