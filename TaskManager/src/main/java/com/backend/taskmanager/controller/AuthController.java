package com.backend.taskmanager.controller;

import com.backend.taskmanager.dto.RegisterRequest;
import com.backend.taskmanager.dto.RegisterResponse;
import com.backend.taskmanager.entity.User;
import com.backend.taskmanager.dto.LoginRequest;
import com.backend.taskmanager.service.AuthServiceImpl;
import com.backend.taskmanager.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            var currentUser = userService.login(request, httpRequest);
            return ResponseEntity.ok(Map.of(
                    "id", currentUser.getId(),
                    "name", currentUser.getName(),
                    "email", currentUser.getEmail()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> currentUser() {

        var user = userService.getCurrentUser();

        if(user != null) {
            return ResponseEntity.ok(Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail()
            ));
        } else {
           return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        try {
            User newUser = authService.register(request);

            RegisterResponse response = RegisterResponse.builder()
                    .success(true)
                    .message("Registration successful! Please login.")
                    .userId(newUser.getId())
                    .username(newUser.getUsername())
                    .email(newUser.getEmail())
                    .role(newUser.getRole().name())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            RegisterResponse response = RegisterResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
        }


