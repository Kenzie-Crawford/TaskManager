package com.backend.taskmanager.dto;

import com.backend.taskmanager.entity.Priority;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class TaskRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100)
    private String title;

    private String description;

    private Priority priority = Priority.MEDIUM;

    @Min(1)
    @Max(1000)
    private Integer points = 10;

    private LocalDate dueDate;

    private Long assignedToId;
}