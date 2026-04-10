package com.backend.taskmanager.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class UpdateUserRequest {
    @Size(min = 3, max = 50)
    private String username;

    @Email
    private String email;

    @Size(min = 1, max = 50)
    private String name;

    private String role;
}