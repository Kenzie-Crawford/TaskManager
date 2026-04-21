package com.backend.taskmanager.controller;


import com.backend.taskmanager.dto.LeaderBoardEntry;
import com.backend.taskmanager.entity.*;
import com.backend.taskmanager.service.AchievementService;
import com.backend.taskmanager.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;



@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;
    private final AchievementService achievementService;

    public UserController(UserService userService, AchievementService achievementService) {
        this.userService = userService;
        this.achievementService = achievementService;
    }

    // Leaderboard - top users sorted by points, with achievement counts
    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderBoardEntry>> getLeaderboard(
            @RequestParam(defaultValue = "20") int limit) {
        List<User> users = userService.getAllUsers();

        AtomicInteger rank = new AtomicInteger(1);
        List<LeaderBoardEntry> leaderboard = users.stream()
                .sorted((a, b) -> b.getTotalPoints() - a.getTotalPoints())
                .limit(limit)
                .map(user -> LeaderBoardEntry.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .name(user.getName())
                        .totalPoints(user.getTotalPoints())
                        .level(user.getLevel())
                        .achievementCount(achievementService.countUserAchievements(user.getId()))
                        .rank(rank.getAndIncrement())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping
    public List <User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUsersById(@PathVariable Long id){
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping ("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new RuntimeException("user not found with email: " + email));
    }

    @GetMapping ("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        try {
            Role userRole = Role.valueOf(role.toUpperCase());
            List<User> users = userService.findByRole(userRole);
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try{
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}/points")
    public ResponseEntity<User> addPoints(@PathVariable Long id, @RequestParam int points) {
        try {
            User updatedUser = userService.addPoints(id, points);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }





}