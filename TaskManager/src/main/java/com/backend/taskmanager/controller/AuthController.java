package com.backend.taskmanager.controller;

import com.backend.taskmanager.dto.LoginResponse;
import com.backend.taskmanager.dto.RegisterRequest;
import com.backend.taskmanager.dto.RegisterResponse;
import com.backend.taskmanager.entity.User;
import com.backend.taskmanager.dto.LoginRequest;
import com.backend.taskmanager.security.JwtTokenProvider;
import com.backend.taskmanager.service.AuthService;
import com.backend.taskmanager.service.UserService;
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

    private final AuthService authService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            User user = authService.login(request);
            String token = jwtTokenProvider.generateToken(user);
            LoginResponse response = LoginResponse.builder()
                    .success(true)
                    .token(token)
                    .tokenType("Bearer")
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole().name())
                    .totalPoints(user.getTotalPoints())
                    .level(user.getLevel())
                    .build();
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            LoginResponse errorResponse = LoginResponse.builder()
                    .success(false)
                    .message("Invalid username or password")
                    .build();

            return ResponseEntity.status(401).body(errorResponse);
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

        User user = userService.getCurrentUser();

        if (user != null) {
            return ResponseEntity.ok(Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "name", user.getName(),
                    "email", user.getEmail(),
                    "role", user.getRole().name(),
                    "totalPoints", user.getTotalPoints(),
                    "level", user.getLevel()

            ));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        System.out.println("Username: " + request.getUsername());
        {
            try {
                User newUser = authService.register(request);

                RegisterResponse response = RegisterResponse.builder()
                        .success(true)
                        .message("Registration successful! Please login.")
                        .userId(newUser.getId())
                        .username(newUser.getUsername())
                        .email(newUser.getEmail())
                        .name(newUser.getName())
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
}


