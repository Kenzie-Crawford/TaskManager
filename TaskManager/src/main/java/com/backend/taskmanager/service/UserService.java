package com.backend.taskmanager.service;

import com.backend.taskmanager.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> getAllUsers();
    Optional <User> findById(Long id);
    Optional <User> findByEmail(String email);
    Optional <User> findByUsername(String username);
    List<User> findByRole(Role.Role role);
    User save(User user);
    void deleteById(Long id);
    User addPoints(Long userId, int points);


}
