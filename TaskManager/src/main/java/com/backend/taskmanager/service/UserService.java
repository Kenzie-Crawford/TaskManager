package com.backend.taskmanager.service;

import com.backend.taskmanager.dto.LoginRequest;
import com.backend.taskmanager.entity.Role;
import com.backend.taskmanager.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> getAllUsers();
    Optional <User> findById(Long id);
    Optional <User> findByEmail(String email);
    Optional <User> findByUsername(String username);
    List<User> findByRole(Role role);
    User save(User user);
    void deleteById(Long id);
    User addPoints(Long userId, int points);
    User login(LoginRequest request, HttpServletRequest httpRequest);
    User getCurrentUser();
}
