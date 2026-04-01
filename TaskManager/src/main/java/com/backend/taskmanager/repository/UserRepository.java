package com.backend.taskmanager.repository;

import com.backend.taskmanager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.taskmanager.entity.Role;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username (used for login)
    Optional<User> findByUsername(String username);

    // Find user by email
    Optional<User> findByEmail(String email);

    // Check if username exists (for registration validation)
    boolean existsByUsername(String username);

    // Check if email exists (for registration validation)
    boolean existsByEmail(String email);

    // Get top users by points for leaderboard
    @Query("SELECT u FROM User u ORDER BY u.totalPoints DESC")
    Page<User> findTopUsersByPoints(Pageable pageable);

    long countByRole(Role role);

    List<User> findByRole (Role role);

}
