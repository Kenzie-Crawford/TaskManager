package com.backend.taskmanager.service;

import com.backend.taskmanager.entity.Achievement;
import com.backend.taskmanager.entity.UserAchievement;
import java.util.List;
import java.util.Optional;

public interface AchievementService {

    // Get all achievements
    List<Achievement> getAllAchievements();

    // Get achievement by ID
    Optional<Achievement> findById(Long id);

    // Get achievements earned by a user
    List<UserAchievement> getUserAchievements(Long userId);

    // Get achievements a user hasn't earned yet
    List<Achievement> getUnearnedAchievements(Long userId);

    // Check and award achievements for a user
    void checkAndAwardAchievements(Long userId);

    // Count how many achievements a user has
    long countUserAchievements(Long userId);

    // Check if user has earned a specific achievement
    boolean hasUserEarnedAchievement(Long userId, Long achievementId);

    // Get progress toward a specific achievement
    int getAchievementProgress(Long userId, Long achievementId);
}