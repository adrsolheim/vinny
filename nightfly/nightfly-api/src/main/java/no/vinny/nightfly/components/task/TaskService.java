package no.vinny.nightfly.components.task;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void create(Task task) {
        taskRepository.insert(task);
    }

    public void create(List<Task> tasks) {
        tasks.forEach(this::create);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findAllUnhandled() {
        return taskRepository.findAllUnhandled();
    }

    public List<Task> findUnhandled(Long entityId, String brewfatherId, TaskType type) {
        return taskRepository.findBy(entityId, brewfatherId, type, false);
    }

    public boolean findAndHandleTask(Long entityId, String brewfatherId, TaskEntity task, TaskType type, String handledBy) {
        if (entityId == null && brewfatherId == null) {
            throw new IllegalArgumentException("Unable to identify tasks without any id");
        }
        List<Long> unhandledTasks = findUnhandled(entityId, brewfatherId, type).stream()
                .filter(t -> task == t.getEntity())
                .filter(t -> type == t.getTask())
                .map(Task::getId)
                .collect(Collectors.toUnmodifiableList());
        if (unhandledTasks.isEmpty()) {
            return false;
        }
        taskRepository.markAsHandled(unhandledTasks, handledBy);
        return true;
    }
}
