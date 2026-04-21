package com.backend.taskmanager.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserAchievementResponse {
    private Long id;
    private Long userId;
    private String username;
    private Long achievementId;
    private String achievementName;
    private String achievementDescription;
    private String badgeIcon;
    private String criteriaType;
    private Integer criteriaValue;
    private LocalDateTime earnedAt;
}
