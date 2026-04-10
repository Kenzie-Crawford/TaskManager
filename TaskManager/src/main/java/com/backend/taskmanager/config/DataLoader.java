package com.backend.taskmanager.config;

import com.backend.taskmanager.entity.*;
import com.backend.taskmanager.repository.AchievementRepository;
import com.backend.taskmanager.repository.TaskRepository;
import com.backend.taskmanager.repository.UserAchievementRepository;
import com.backend.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("========================================");
        System.out.println("        DATA LOADER STARTED             ");
        System.out.println("========================================");

        loadAchievements();
        loadUsers();
        loadTasks();
        loadUserAchievements();

        System.out.println("========================================");
        System.out.println("        DATA LOADER COMPLETED           ");
        System.out.println("========================================");
    }

    private void loadAchievements() {
        if (achievementRepository.count() > 0) {
            System.out.println("⚠️ Achievements already exist - skipping");
            return;
        }

        System.out.println("\n📦 Loading Achievements...");

        List<Achievement> achievements = Arrays.asList(
                createAchievement("Task Master", "Complete 10 tasks",
                        AchievementCriteria.TASKS_COMPLETED, 10, "🏆"),
                createAchievement("Task Champion", "Complete 50 tasks",
                        AchievementCriteria.TASKS_COMPLETED, 50, "🏆🏆"),
                createAchievement("Point Hunter", "Earn 500 points",
                        AchievementCriteria.TOTAL_POINTS, 500, "⭐"),
                createAchievement("Point Legend", "Earn 1000 points",
                        AchievementCriteria.TOTAL_POINTS, 1000, "🌟🌟🌟"),
                createAchievement("Achievement Hunter", "Earn 5 achievements",
                        AchievementCriteria.ACHIEVEMENTS_COUNT, 5, "🏆🏆🏆")
        );

        achievementRepository.saveAll(achievements);
        System.out.println("✅ Loaded " + achievements.size() + " achievements");
    }

    private void loadUsers() {
        if (userRepository.count() > 0) {
            System.out.println("⚠️ Users already exist - skipping");
            return;
        }

        System.out.println("\n👥 Loading Users...");

        List<User> users = Arrays.asList(
                createUser("john_doe", "john@example.com", "password123", "John Doe",
                        Role.EMPLOYEE, 150, 2),
                createUser("jane_smith", "jane@example.com", "password123", "Jane Smith",
                        Role.EMPLOYEE, 320, 4),
                createUser("bob_wilson", "bob@example.com", "password123", "Bob Wilson",
                        Role.EMPLOYEE, 75, 1),
                createUser("alice_manager", "alice@example.com", "password123", "Alice Johnson",
                        Role.MANAGER, 500, 6),
                createUser("admin_user", "admin@example.com", "admin123", "Admin User",
                        Role.ADMIN, 1000, 11)
        );

        userRepository.saveAll(users);
        System.out.println("✅ Loaded " + users.size() + " users");
        System.out.println("   📝 Credentials:");
        System.out.println("      - john_doe / password123 (EMPLOYEE)");
        System.out.println("      - jane_smith / password123 (EMPLOYEE)");
        System.out.println("      - bob_wilson / password123 (EMPLOYEE)");
        System.out.println("      - alice_manager / password123 (MANAGER)");
        System.out.println("      - admin_user / admin123 (ADMIN)");
    }

    private void loadTasks() {
        if (taskRepository.count() > 0) {
            System.out.println("⚠️ Tasks already exist - skipping");
            return;
        }

        System.out.println("\n📋 Loading Tasks...");

        User john = userRepository.findByUsername("john_doe").orElse(null);
        User jane = userRepository.findByUsername("jane_smith").orElse(null);
        User bob = userRepository.findByUsername("bob_wilson").orElse(null);
        User alice = userRepository.findByUsername("alice_manager").orElse(null);

        if (john == null || jane == null || bob == null || alice == null) {
            System.out.println("❌ Error: Users not found - cannot create tasks");
            return;
        }

        List<Task> tasks = Arrays.asList(
                createTask("Complete project documentation",
                        "Write technical documentation for the REST API",
                        TaskStatus.PENDING, Priority.HIGH, 50,
                        LocalDate.now().plusDays(3), john, alice),

                createTask("Fix login bug",
                        "User authentication failing on mobile devices",
                        TaskStatus.IN_PROGRESS, Priority.URGENT, 100,
                        LocalDate.now().plusDays(1), john, alice),

                createTask("Design database schema",
                        "Create ERD for the new reporting feature",
                        TaskStatus.COMPLETED, Priority.MEDIUM, 40,
                        LocalDate.now().minusDays(2), john, alice),

                createTask("Write unit tests",
                        "Achieve 80% code coverage for the service layer",
                        TaskStatus.PENDING, Priority.MEDIUM, 60,
                        LocalDate.now().plusDays(5), jane, alice),

                createTask("Review pull requests",
                        "Review and approve team PRs",
                        TaskStatus.PENDING, Priority.HIGH, 30,
                        LocalDate.now().plusDays(1), jane, alice),

                createTask("Update documentation",
                        "Refresh outdated API documentation",
                        TaskStatus.PENDING, Priority.LOW, 20,
                        LocalDate.now().plusDays(7), bob, alice),

                createTask("Performance testing",
                        "Load test the API with 1000 concurrent users",
                        TaskStatus.COMPLETED, Priority.HIGH, 80,
                        LocalDate.now().minusDays(1), bob, alice),

                createTask("Security audit",
                        "Review code for security vulnerabilities",
                        TaskStatus.PENDING, Priority.URGENT, 120,
                        LocalDate.now().plusDays(10), jane, alice)
        );

        taskRepository.saveAll(tasks);
        System.out.println("✅ Loaded " + tasks.size() + " tasks");
    }

    private void loadUserAchievements() {
        if (userAchievementRepository.count() > 0) {
            System.out.println("⚠️ User achievements already exist - skipping");
            return;
        }

        System.out.println("\n🏆 Loading User Achievements...");

        User john = userRepository.findByUsername("john_doe").orElse(null);
        User jane = userRepository.findByUsername("jane_smith").orElse(null);
        Achievement taskMaster = achievementRepository.findByName("Task Master").orElse(null);
        Achievement pointHunter = achievementRepository.findByName("Point Hunter").orElse(null);

        if (john == null || jane == null || taskMaster == null) {
            System.out.println("⚠️ Users or achievements not found - skipping");
            return;
        }

        int count = 0;

        // Award Task Master to John
        UserAchievement johnAchievement = new UserAchievement();
        johnAchievement.setUser(john);
        johnAchievement.setAchievement(taskMaster);
        johnAchievement.setEarnedAt(LocalDateTime.now().minusDays(5));
        userAchievementRepository.save(johnAchievement);
        count++;

        // Award Point Hunter to Jane
        if (pointHunter != null) {
            UserAchievement janeAchievement = new UserAchievement();
            janeAchievement.setUser(jane);
            janeAchievement.setAchievement(pointHunter);
            janeAchievement.setEarnedAt(LocalDateTime.now().minusDays(2));
            userAchievementRepository.save(janeAchievement);
            count++;
        }

        System.out.println("✅ Loaded " + count + " user-achievement relationships");
    }

    // ===== HELPER METHODS =====

    private Achievement createAchievement(String name, String description,
                                          AchievementCriteria criteria, int value, String icon) {
        Achievement achievement = new Achievement();
        achievement.setName(name);
        achievement.setDescription(description);
        achievement.setCriteriaType(criteria);
        achievement.setCriteriaValue(value);
        achievement.setBadgeIcon(icon);
        achievement.setCreatedAt(LocalDateTime.now());
        return achievement;
    }

    private User createUser(String username, String email, String rawPassword,
                            String name, Role role, int totalPoints, int level) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setName(name);
        user.setRole(role);
        user.setTotalPoints(totalPoints);
        user.setLevel(level);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    private Task createTask(String title, String description, TaskStatus status,
                            Priority priority, int points, LocalDate dueDate,
                            User assignedTo, User createdBy) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        task.setPoints(points);
        task.setDueDate(dueDate);
        task.setAssignedTo(assignedTo);
        task.setCreatedBy(createdBy);

        if (status == TaskStatus.COMPLETED) {
            task.setCompletedAt(LocalDateTime.now().minusDays(1));
        }

        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return task;
    }
}