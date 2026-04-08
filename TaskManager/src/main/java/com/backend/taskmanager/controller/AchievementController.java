package com.backend.taskmanager.controller;

import com.backend.taskmanager.entity.Achievement;
import com.backend.taskmanager.entity.UserAchievement;
import com.backend.taskmanager.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;
    //get all achievements
    @GetMapping
    public ResponseEntity<List<Achievement>> getAllAchievements() {
        return ResponseEntity.ok(achievementService.getAllAchievements());
    }

    //Get a specific achievement by ID

    @GetMapping("/{id}")
    public ResponseEntity<Achievement> getAchievementById(@PathVariable Long id) {
        return achievementService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Get all achievements earned by a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAchievement>> getUserAchievements(@PathVariable Long userId) {
        return ResponseEntity.ok(achievementService.getUserAchievements(userId));
    }

    //Get achievements a user has NOT earned yet

    @GetMapping("/user/{userId}/available")
    public ResponseEntity<List<Achievement>> getAvailableAchievements(@PathVariable Long userId) {
        return ResponseEntity.ok(achievementService.getUnearnedAchievements(userId));
    }

    //Count how many achievements a user has earned

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countUserAchievements(@PathVariable Long userId) {
        return ResponseEntity.ok(achievementService.countUserAchievements(userId));
    }

    //Check if a user has earned a specific achievement

    @GetMapping("/user/{userId}/has/{achievementId}")
    public ResponseEntity<Boolean> hasUserEarnedAchievement(
            @PathVariable Long userId,
            @PathVariable Long achievementId) {
        boolean hasEarned = achievementService.hasUserEarnedAchievement(userId, achievementId);
        return ResponseEntity.ok(hasEarned);
    }

    //Get progress toward a specific achievement (percentage)

    @GetMapping("/user/{userId}/progress/{achievementId}")
    public ResponseEntity<Integer> getAchievementProgress(
            @PathVariable Long userId,
            @PathVariable Long achievementId) {
        int progress = achievementService.getAchievementProgress(userId, achievementId);
        return ResponseEntity.ok(progress);
    }



    //Manually trigger achievement check for a user
    @PostMapping("/user/{userId}/check")
    public ResponseEntity<String> checkAndAwardAchievements(@PathVariable Long userId) {
        achievementService.checkAndAwardAchievements(userId);
        return ResponseEntity.ok("Achievements checked and awarded for user " + userId);
    }
}