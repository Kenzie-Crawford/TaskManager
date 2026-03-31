package com.backend.taskmanager.repository;

import com.backend.taskmanager.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    Optional<Achievement> findByName(String name);

    List<Achievement> findByNameContainingIgnoreCase(String name);

    List<Achievement> findByCriteriaType(AchievementCriteria criteriaType);

    List<Achievement> findByCriteriaValue(Integer criteriaValue);

    Optional<Achievement> findByCriteriaTypeAndCriteriaValue(AchievementCriteria criteriaType, Integer criteriaValue);


}
