package com.backend.taskmanager.repository;

import com.backend.taskmanager.entity.Task;
import com.backend.taskmanager.entity.TaskStatus;
import com.backend.taskmanager.entity.Priority;
import com.backend.taskmanager.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByPriority(Priority priority);
    List<Task> findByAssignedTo(User user);
    List<Task> findByAssignedToId(Long userId);
    List<Task> findByStatus(TaskStatus status);
    long countByAssignedToAndStatus(User user, TaskStatus status);
    List<Task> findByStatusAndPriority(TaskStatus status, Priority priority);
    List<Task> findByAssignedToAndStatus(User user, TaskStatus status);

    // Custom Query for finding active tasks
    @Query("SELECT t FROM Task t WHERE t.assignedTo.id = :userId AND t.status != 'COMPLETED'")
    List<Task> findActiveTasksByUser(@Param("userId") Long userId);

    //  Custom Query for counting completed tasks in date range
    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignedTo.id = :userId " +
            "AND t.status = 'COMPLETED' " +
            "AND t.completedAt BETWEEN :startDate AND :endDate")
    Long countCompletedTasksInDateRange(@Param("userId") Long userId,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.status = 'COMPLETED', t.completedAt = CURRENT_TIMESTAMP " +
            "WHERE t.id = :taskId AND t.assignedTo.id = :userId")
    int completeTask(@Param("taskId") Long taskId, @Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignedTo.id = :userId AND t.status != 'COMPLETED'")
    long countActiveTasksByUser(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.assignedTo IS NULL")
    List<Task> findUnassignedTasks();

}