package com.backend.taskmanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaderBoardEntry {
    private Long userId;
    private String username;
    private String name;
    private Integer totalPoints;
    private Integer level;
    private Long achievementCount;
    private Integer rank;
}
