package no.vinny.nightfly.components.task;

import org.springframework.stereotype.Service;

import java.util.List;

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
}
