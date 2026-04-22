package com.backend.taskmanager.repository;

import com.backend.taskmanager.entity.Achievement;
import com.backend.taskmanager.entity.AchievementCriteria;
import com.backend.taskmanager.entity.User;
import com.backend.taskmanager.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {

    Optional<UserAchievement> findByUserAndAchievement(User user, Achievement achievement);

    List<UserAchievement> findByUser(User user);

    long countByUser(User user);

    void deleteAll();

    List<UserAchievement> findByAchievement(Achievement achievement);

    List<UserAchievement> findByUserAndEarnedAtBetween(User user, LocalDateTime start, LocalDateTime end);

    // Get achievements earned by a user after a specific date
    List<UserAchievement> findByUserAndEarnedAtAfter(User user, LocalDateTime date);

    // Get all achievements a user has NOT earned yet
    @Query("SELECT a FROM Achievement a WHERE a NOT IN " +
            "(SELECT ua.achievement FROM UserAchievement ua WHERE ua.user = :user)")
    List<Achievement> findUnearnedAchievementsByUser(@Param("user") User user);

    // Check if user has earned any achievement of a specific criteria type
    @Query("SELECT COUNT(ua) > 0 FROM UserAchievement ua " +
            "WHERE ua.user = :user AND ua.achievement.criteriaType = :criteriaType")
    boolean hasAchievementOfType(@Param("user") User user,
                                 @Param("criteriaType") AchievementCriteria criteriaType);

    // Get the most recently earned achievements for a user
    List<UserAchievement> findTop5ByUserOrderByEarnedAtDesc(User user);


}
