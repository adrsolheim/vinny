package no.vinny.nightfly.components.task;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> tasks() {
        return taskService.findAll();
    }

    @GetMapping("/unhandled")
    public List<Task> unhandledTasks() {
        return taskService.findAllUnhandled();
    }
}
