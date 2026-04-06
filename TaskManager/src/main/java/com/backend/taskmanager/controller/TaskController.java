package com.backend.taskmanager.controller;

import com.backend.taskmanager.entity.Priority;
import com.backend.taskmanager.entity.Task;
import com.backend.taskmanager.entity.TaskStatus;
import com.backend.taskmanager.entity.User;
import com.backend.taskmanager.service.TaskService;
import com.backend.taskmanager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTasksById(@PathVariable Long id) {
        return taskService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        try {
            Task updatedTask = taskService.updateTask(id, taskDetails);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping ("/user/{userId}")
    public List<Task> getTaskByUser(@PathVariable Long userId) {
        return taskService.findByAssignedToId(userId);
    }

    @GetMapping ("/user/{userId}/active")
    public List <Task> getActiveTasksByUser(@PathVariable Long userId) {
        return taskService.findActiveTasksByUser(userId);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public List<Task> getTasksByUserAndStatus(@PathVariable Long userId,
                                              @PathVariable TaskStatus status) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return taskService.findByAssignedToAndStatus(user, status);
    }

    @GetMapping ("user/{userId}/count/status/{status}")
    public long countTasksByUserAndStatus(@PathVariable Long userId, @PathVariable TaskStatus status) {
        User user = userService.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));
        return taskService.countByAssignedToAndStatus(user, status);
    }

    @GetMapping("/status/{status}")
    public List<Task> getTasksByStatus(@PathVariable TaskStatus status) {
        return taskService.findByStatus(status);
    }

    @GetMapping("/priority/{priority}")
    public List<Task> getTasksByPriority(@PathVariable Priority priority) {
        return taskService.findByPriority(priority);
    }

    @GetMapping("/filter")
    public List<Task> getTasksByStatusAndPriority(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Priority priority) {

        if (status != null && priority != null) {
            return taskService.findByStatusAndPriority(status, priority);
        } else if (status != null) {
            return taskService.findByStatus(status);
        } else if (priority != null) {
            return taskService.findByPriority(priority);
        } else {
            return taskService.getAllTasks();
        }
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<Task> assignTask(@PathVariable Long id, @RequestParam Long userId) {
        try {
            Task updatedTask = taskService.assignTask(id, userId);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Long id, @RequestParam Long userId) {
        try {
            Task completedTask = taskService.completeTask(id, userId);
            return ResponseEntity.ok(completedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

