package com.backend.taskmanager.service;

import com.backend.taskmanager.dto.LoginRequest;
import com.backend.taskmanager.dto.RegisterRequest;
import com.backend.taskmanager.entity.Role;
import com.backend.taskmanager.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("new@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setName("New User");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setName("Test User");
        testUser.setRole(Role.EMPLOYEE);
        testUser.setTotalPoints(0);
        testUser.setLevel(1);
    }

    @Test
    void register_ShouldCreateNewUser_WhenValidRequest() {
        when(userService.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userService.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userService.save(any(User.class))).thenReturn(testUser);

        User result = authService.register(registerRequest);

        assertNotNull(result);
        verify(userService).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenUsernameExists() {
        when(userService.findByUsername("newuser")).thenReturn(Optional.of(testUser));

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        when(userService.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userService.findByEmail("new@example.com")).thenReturn(Optional.of(testUser));

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
    }

    @Test
    void login_ShouldReturnUser_WhenCredentialsValid() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        User result = authService.login(loginRequest);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }
}