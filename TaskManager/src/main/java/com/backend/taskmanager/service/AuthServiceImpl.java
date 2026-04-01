package com.backend.taskmanager.service;

import com.backend.taskmanager.dto.RegisterRequest;
import com.backend.taskmanager.dto.LoginRequest;
import com.backend.taskmanager.entity.Role;
import com.backend.taskmanager.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


    @Service
    @RequiredArgsConstructor
    public class AuthServiceImpl implements AuthService {

        private final AuthenticationManager authenticationManager;
        private final UserService userService;
        private final PasswordEncoder passwordEncoder;

        @Override
        public User login(LoginRequest request) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            return userService.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        @Override
        public User register(RegisterRequest request) {
            // Check if username exists
            if (userService.findByUsername(request.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            }

            // Check if email exists
            if (userService.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists");
            }

            // Create new user
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.EMPLOYEE);
            user.setTotalPoints(0);
            user.setLevel(1);

            return userService.save(user);
        }
    }

