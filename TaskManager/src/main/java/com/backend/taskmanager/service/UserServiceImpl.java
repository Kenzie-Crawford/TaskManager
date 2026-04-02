package com.backend.taskmanager.service;

import com.backend.taskmanager.entity.User;
import com.backend.taskmanager.entity.Role;
import com.backend.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // ===== ADD THIS MISSING METHOD =====
    @Override
    public User addPoints(Long userId, int points) {
        User user = findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setTotalPoints(user.getTotalPoints() + points);

        // Update level: every 100 points = 1 level
        int newLevel = (user.getTotalPoints() / 100) + 1;
        user.setLevel(newLevel);

        return userRepository.save(user);
    }
}