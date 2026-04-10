package com.backend.taskmanager.service;

import com.backend.taskmanager.dto.LoginRequest;
import com.backend.taskmanager.dto.RegisterRequest;
import com.backend.taskmanager.entity.User;

public interface AuthService {
    User login(LoginRequest request);
    User register(RegisterRequest request);

}
