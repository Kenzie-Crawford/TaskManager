package com.backend.taskmanager.service;

import com.backend.taskmanager.entity.*;
import com.backend.taskmanager.repository.AchievementRepository;
import com.backend.taskmanager.repository.UserAchievementRepository;
import com.backend.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserService userService;
    private final TaskService taskService;

    @Override
    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    @Override
    public Optional<Achievement> findById(Long id) {
        return achievementRepository.findById(id);
    }

    @Override
    public List<UserAchievement> getUserAchievements(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userAchievementRepository.findByUser(user);
    }

    @Override
    public List<Achievement> getUnearnedAchievements(Long userId) {
        List<Achievement> allAchievements = achievementRepository.findAll();
        List<Achievement> earnedAchievements = getUserAchievements(userId).stream()
                .map(UserAchievement::getAchievement)
                .collect(Collectors.toList());

        return allAchievements.stream()
                .filter(achievement -> !earnedAchievements.contains(achievement))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void checkAndAwardAchievements(Long userId) {
        List<Achievement> allAchievements = achievementRepository.findAll();

        for (Achievement achievement : allAchievements) {
            // Skip if already earned
            if (hasUserEarnedAchievement(userId, achievement.getId())) {
                continue;
            }

            // Check if user meets criteria
            if (doesUserMeetCriteria(userId, achievement)) {
                awardAchievement(userId, achievement);
            }
        }
    }

    @Override
    public long countUserAchievements(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userAchievementRepository.countByUser(user);
    }

    @Override
    public boolean hasUserEarnedAchievement(Long userId, Long achievementId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Achievement achievement = findById(achievementId)
                .orElseThrow(() -> new RuntimeException("Achievement not found"));
        return userAchievementRepository.findByUserAndAchievement(user, achievement).isPresent();
    }

    @Override
    public int getAchievementProgress(Long userId, Long achievementId) {
        Achievement achievement = findById(achievementId)
                .orElseThrow(() -> new RuntimeException("Achievement not found"));

        int currentValue = getCurrentValueForCriteria(userId, achievement.getCriteriaType());
        int requiredValue = achievement.getCriteriaValue();

        // Return progress as percentage (0-100)
        return Math.min(100, (currentValue * 100) / requiredValue);
    }

    private boolean doesUserMeetCriteria(Long userId, Achievement achievement) {
        AchievementCriteria criteria = achievement.getCriteriaType();
        int requiredValue = achievement.getCriteriaValue();
        int currentValue = getCurrentValueForCriteria(userId, criteria);

        return currentValue >= requiredValue;
    }

    private int getCurrentValueForCriteria(Long userId, AchievementCriteria criteria) {
        switch (criteria) {
            case TASKS_COMPLETED:
                return getCompletedTaskCount(userId);
            case TOTAL_POINTS:
                User user = userService.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                return user.getTotalPoints();
            case ACHIEVEMENTS_COUNT:
                return (int) countUserAchievements(userId);
            default:
                return 0;
        }
    }

    private int getCompletedTaskCount(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return (int) taskService.countByAssignedToAndStatus(user, TaskStatus.COMPLETED);
    }


    private void awardAchievement(Long userId, Achievement achievement) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUser(user);
        userAchievement.setAchievement(achievement);
        userAchievement.setEarnedAt(LocalDateTime.now());

        userAchievementRepository.save(userAchievement);

        // Bonus points for earning achievement!
        userService.addPoints(userId, 50);

        System.out.println("🎉 User " + user.getUsername() + " earned achievement: " + achievement.getName());
    }

        @Scheduled(cron = "0 0 0 21 * *")
        @Transactional
        public void resetMonthlyAchievements() {
            userAchievementRepository.deleteAll();
            System.out.println("✅ Monthly achievements reset complete");
    }
}