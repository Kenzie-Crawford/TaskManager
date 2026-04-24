package com.backend.taskmanager.service;

import com.backend.taskmanager.entity.Priority;
import com.backend.taskmanager.entity.Task;
import com.backend.taskmanager.entity.TaskStatus;
import com.backend.taskmanager.entity.User;

import java.util.List;
import java.util.Optional;

public interface TaskService {


    List<Task> getAllTasks();
    Optional<Task> findById(Long id);
    Task createTask(Task task);
    Task updateTask(Long id, Task task);
    void deleteTask(Long id);

    List<Task> findByAssignedTo(User user);
    List<Task> findByAssignedToId(Long userId);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByPriority(Priority priority);
    List<Task> findByStatusAndPriority(TaskStatus status, Priority priority);
    List<Task> findByAssignedToAndStatus(User user, TaskStatus status);
    long countByAssignedToAndStatus(User user, TaskStatus status);
    List<Task> findActiveTasksByUser(Long userId);


    Task assignTask(Long taskId, Long userId);
    Task completeTask(Long taskId, Long userId);

    List<Task> getMissionBoardTasks();  // Get all unassigned tasks
    Task claimTask(Long taskId, Long userId);
    public Task startTask(Long taskId, Long userId);
}