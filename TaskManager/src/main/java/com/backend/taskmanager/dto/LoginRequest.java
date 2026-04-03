package com.backend.taskmanager.dto;

import com.backend.taskmanager.entity.Priority;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;


}
