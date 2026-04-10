package com.backend.taskmanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchievementResponse {
    private Long id;
    private String name;
    private String description;
    private String criteriaType;
    private Integer criteriaValue;
    private String badgeIcon;
}