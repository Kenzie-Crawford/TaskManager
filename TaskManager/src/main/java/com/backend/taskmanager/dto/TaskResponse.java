package com.backend.taskmanager.dto;

import com.backend.taskmanager.entity.Priority;
import com.backend.taskmanager.entity.TaskStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private Integer points;
    private LocalDate dueDate;
    private UserSummary assignedTo;
    private UserSummary createdBy;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
}

@Data
@Builder
class UserSummary {
    private Long id;
    private String username;
}