package no.vinny.nightfly.components.task;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class Task {
    private Long id;
    private String brewfatherId;
    private ZonedDateTime handled;
    private String handledBy;
    private Long entityId;
    private TaskEntity entity;
    private TaskOrigin origin;
    private TaskType task;
}
