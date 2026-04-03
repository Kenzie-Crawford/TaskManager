package com.backend.taskmanager.service;

import com.backend.taskmanager.entity.*;
import com.backend.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task createTask(Task task) {
        // Validate creator exists
        if (task.getCreatedBy() == null || task.getCreatedBy().getId() == null) {
            throw new RuntimeException("Task must have a creator");
        }

        User creator = userService.findById(task.getCreatedBy().getId())
                .orElseThrow(() -> new RuntimeException("Creator not found with id: " + task.getCreatedBy().getId()));
        task.setCreatedBy(creator);

        // Handle assigned user if present
        if (task.getAssignedTo() != null && task.getAssignedTo().getId() != null) {
            User assignee = userService.findById(task.getAssignedTo().getId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found with id: " + task.getAssignedTo().getId()));
            task.setAssignedTo(assignee);
        }

        // Set default status if not set
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
        }

        // Set default priority if not set
        if (task.getPriority() == null) {
            task.setPriority(Priority.MEDIUM);
        }

        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, Task taskDetails) {
        Task existingTask = findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        // Update fields only if provided
        if (taskDetails.getTitle() != null) {
            existingTask.setTitle(taskDetails.getTitle());
        }
        if (taskDetails.getDescription() != null) {
            existingTask.setDescription(taskDetails.getDescription());
        }
        if (taskDetails.getStatus() != null) {
            existingTask.setStatus(taskDetails.getStatus());
        }
        if (taskDetails.getPriority() != null) {
            existingTask.setPriority(taskDetails.getPriority());
        }
        if (taskDetails.getPoints() != null) {
            existingTask.setPoints(taskDetails.getPoints());
        }
        if (taskDetails.getDueDate() != null) {
            existingTask.setDueDate(taskDetails.getDueDate());
        }

        // Update assigned user if provided
        if (taskDetails.getAssignedTo() != null && taskDetails.getAssignedTo().getId() != null) {
            User assignee = userService.findById(taskDetails.getAssignedTo().getId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found with id: " + taskDetails.getAssignedTo().getId()));
            existingTask.setAssignedTo(assignee);
        }

        return taskRepository.save(existingTask);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    // ===== Query Methods =====

    @Override
    public List<Task> findByAssignedTo(User user) {
        if (user == null || user.getId() == null) {
            throw new RuntimeException("User cannot be null");
        }
        return taskRepository.findByAssignedTo(user);
    }

    @Override
    public List<Task> findByAssignedToId(Long userId) {
        if (userId == null) {
            throw new RuntimeException("User ID cannot be null");
        }
        return taskRepository.findByAssignedToId(userId);
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        if (status == null) {
            throw new RuntimeException("Status cannot be null");
        }
        return taskRepository.findByStatus(status);
    }

    @Override
    public List<Task> findByPriority(Priority priority) {
        if (priority == null) {
            throw new RuntimeException("Priority cannot be null");
        }
        return taskRepository.findByPriority(priority);
    }

    @Override
    public List<Task> findByStatusAndPriority(TaskStatus status, Priority priority) {
        if (status == null || priority == null) {
            throw new RuntimeException("Status and Priority cannot be null");
        }
        return taskRepository.findByStatusAndPriority(status, priority);
    }

    @Override
    public List<Task> findByAssignedToAndStatus(User user, TaskStatus status) {
        if (user == null || status == null) {
            throw new RuntimeException("User and Status cannot be null");
        }
        return taskRepository.findByAssignedToAndStatus(user, status);
    }

    @Override
    public long countByAssignedToAndStatus(User user, TaskStatus status) {
        if (user == null || status == null) {
            throw new RuntimeException("User and Status cannot be null");
        }
        return taskRepository.countByAssignedToAndStatus(user, status);
    }

    @Override
    public List<Task> findActiveTasksByUser(Long userId) {
        // Verify user exists
        userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return taskRepository.findActiveTasksByUser(userId);
    }

    // ===== Task Operations =====

    @Override
    @Transactional
    public Task assignTask(Long taskId, Long userId) {
        Task task = findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        task.setAssignedTo(user);
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task completeTask(Long taskId, Long userId) {
        // Get the task to validate and get points
        Task task = findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        // Verify task is assigned to this user
        if (task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(userId)) {
            throw new RuntimeException("Task is not assigned to user: " + userId);
        }

        // Check if already completed
        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new RuntimeException("Task already completed");
        }

        // Execute the update - returns number of rows affected
        int updatedRows = taskRepository.completeTask(taskId, userId);

        if (updatedRows > 0) {
            // Award points to user
            userService.addPoints(userId, task.getPoints());

            // Return the updated task
            return findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found after completion"));
        } else {
            throw new RuntimeException("Failed to complete task");
        }
    }
}