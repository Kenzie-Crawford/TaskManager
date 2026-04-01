package com.backend.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterResponse {
    private String message;
    private Long userId;
    private String username;
    private String email;
    private String role;
    private boolean success;


}