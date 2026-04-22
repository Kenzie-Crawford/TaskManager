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
        loadUnassignedTasks();
        loadUserAchievements();

        System.out.println("========================================");
        System.out.println("        DATA LOADER COMPLETED           ");
        System.out.println("========================================");
    }

    // ─────────────────────────────────────────────
    //  ACHIEVEMENTS
    // ─────────────────────────────────────────────
    private void loadAchievements() {
        if (achievementRepository.count() > 0) {
            System.out.println("⚠️  Achievements already exist - skipping");
            return;
        }

        System.out.println("\n📦 Loading Achievements...");

        List<Achievement> achievements = Arrays.asList(
                createAchievement("First Step",       "Complete your first task",          AchievementCriteria.TASKS_COMPLETED,  1,    "🥉"),
                createAchievement("Task Master",       "Complete 10 tasks",                 AchievementCriteria.TASKS_COMPLETED,  10,   "🏆"),
                createAchievement("Task Champion",     "Complete 25 tasks",                 AchievementCriteria.TASKS_COMPLETED,  25,   "🏆🏆"),
                createAchievement("Task Legend",       "Complete 50 tasks",                 AchievementCriteria.TASKS_COMPLETED,  50,   "🏆🏆🏆"),
                createAchievement("Point Collector",   "Earn 100 points",                   AchievementCriteria.TOTAL_POINTS,     100,  "⭐"),
                createAchievement("Point Hunter",      "Earn 500 points",                   AchievementCriteria.TOTAL_POINTS,     500,  "🌟"),
                createAchievement("Point Legend",      "Earn 1000 points",                  AchievementCriteria.TOTAL_POINTS,     1000, "🌟🌟🌟"),
                createAchievement("Achievement Hunter","Earn 5 achievements",               AchievementCriteria.ACHIEVEMENTS_COUNT, 5,  "🎯")
        );

        achievementRepository.saveAll(achievements);
        System.out.println("✅ Loaded " + achievements.size() + " achievements");
    }

    // ─────────────────────────────────────────────
    //  USERS
    // ─────────────────────────────────────────────
    private void loadUsers() {
        if (userRepository.count() > 0) {
            System.out.println("⚠️  Users already exist - skipping");
            return;
        }

        System.out.println("\n👥 Loading Users...");

        List<User> users = Arrays.asList(
                // Employees – varied points so leaderboard is interesting
                createUser("john_doe",      "john@example.com",    "password123", "John Doe",       Role.EMPLOYEE, 480,  5),
                createUser("jane_smith",    "jane@example.com",    "password123", "Jane Smith",     Role.EMPLOYEE, 820,  9),
                createUser("bob_wilson",    "bob@example.com",     "password123", "Bob Wilson",     Role.EMPLOYEE, 210,  3),
                createUser("sara_jones",    "sara@example.com",    "password123", "Sara Jones",     Role.EMPLOYEE, 650,  7),
                createUser("mike_chen",     "mike@example.com",    "password123", "Mike Chen",      Role.EMPLOYEE, 130,  2),
                createUser("lisa_park",     "lisa@example.com",    "password123", "Lisa Park",      Role.EMPLOYEE, 970,  10),
                createUser("tom_harris",    "tom@example.com",     "password123", "Tom Harris",     Role.EMPLOYEE, 55,   1),
                // Manager & Admin
                createUser("alice_manager", "alice@example.com",   "password123", "Alice Johnson",  Role.MANAGER,  500,  6),
                createUser("admin_user",    "admin@example.com",   "admin123",    "Admin User",     Role.ADMIN,    1200, 13)
        );

        userRepository.saveAll(users);

        System.out.println("✅ Loaded " + users.size() + " users");
        System.out.println("   📝 Credentials:");
        System.out.println("      - john_doe      / password123  (EMPLOYEE)");
        System.out.println("      - jane_smith    / password123  (EMPLOYEE)");
        System.out.println("      - bob_wilson    / password123  (EMPLOYEE)");
        System.out.println("      - sara_jones    / password123  (EMPLOYEE)");
        System.out.println("      - mike_chen     / password123  (EMPLOYEE)");
        System.out.println("      - lisa_park     / password123  (EMPLOYEE)");
        System.out.println("      - tom_harris    / password123  (EMPLOYEE)");
        System.out.println("      - alice_manager / password123  (MANAGER)");
        System.out.println("      - admin_user    / admin123     (ADMIN)");
    }

    // ─────────────────────────────────────────────
    //  ASSIGNED TASKS
    // ─────────────────────────────────────────────
    private void loadTasks() {
        if (taskRepository.count() > 0) {
            System.out.println("⚠️  Tasks already exist - skipping");
            return;
        }

        System.out.println("\n📋 Loading Assigned Tasks...");

        User john  = userRepository.findByUsername("john_doe").orElse(null);
        User jane  = userRepository.findByUsername("jane_smith").orElse(null);
        User bob   = userRepository.findByUsername("bob_wilson").orElse(null);
        User sara  = userRepository.findByUsername("sara_jones").orElse(null);
        User mike  = userRepository.findByUsername("mike_chen").orElse(null);
        User lisa  = userRepository.findByUsername("lisa_park").orElse(null);
        User tom   = userRepository.findByUsername("tom_harris").orElse(null);
        User alice = userRepository.findByUsername("alice_manager").orElse(null);

        if (john == null || alice == null) {
            System.out.println("❌ Users not found – cannot create tasks");
            return;
        }

        List<Task> tasks = Arrays.asList(

                // ── John ──
                createTask("Complete project documentation",
                        "Write technical documentation for the REST API endpoints",
                        TaskStatus.PENDING, Priority.HIGH, 50,
                        LocalDate.now().plusDays(3), john, alice),

                createTask("Fix login bug",
                        "User authentication failing on mobile devices – investigate JWT expiry",
                        TaskStatus.IN_PROGRESS, Priority.URGENT, 100,
                        LocalDate.now().plusDays(1), john, alice),

                createTask("Design database schema",
                        "Create ERD for the new reporting feature",
                        TaskStatus.COMPLETED, Priority.MEDIUM, 40,
                        LocalDate.now().minusDays(5), john, alice),

                createTask("Refactor auth service",
                        "Clean up AuthServiceImpl – extract token logic into a utility class",
                        TaskStatus.COMPLETED, Priority.MEDIUM, 60,
                        LocalDate.now().minusDays(3), john, alice),

                createTask("Set up CI pipeline",
                        "Configure GitHub Actions to run unit tests on every push",
                        TaskStatus.PENDING, Priority.HIGH, 80,
                        LocalDate.now().plusDays(6), john, alice),

                // ── Jane ──
                createTask("Write unit tests",
                        "Achieve 80% code coverage for the service layer",
                        TaskStatus.COMPLETED, Priority.MEDIUM, 60,
                        LocalDate.now().minusDays(4), jane, alice),

                createTask("Review pull requests",
                        "Review and approve team PRs before Friday release",
                        TaskStatus.COMPLETED, Priority.HIGH, 30,
                        LocalDate.now().minusDays(2), jane, alice),

                createTask("Security audit",
                        "Review code for OWASP Top 10 security vulnerabilities",
                        TaskStatus.IN_PROGRESS, Priority.URGENT, 120,
                        LocalDate.now().plusDays(2), jane, alice),

                createTask("Implement rate limiting",
                        "Add rate limiting to auth endpoints to prevent brute force",
                        TaskStatus.PENDING, Priority.HIGH, 90,
                        LocalDate.now().plusDays(4), jane, alice),

                createTask("API integration tests",
                        "Write Postman collection covering all REST endpoints",
                        TaskStatus.COMPLETED, Priority.MEDIUM, 50,
                        LocalDate.now().minusDays(6), jane, alice),

                // ── Bob ──
                createTask("Update API documentation",
                        "Refresh outdated Swagger docs to match current endpoints",
                        TaskStatus.PENDING, Priority.LOW, 20,
                        LocalDate.now().plusDays(7), bob, alice),

                createTask("Performance testing",
                        "Load test the API with 1000 concurrent users using JMeter",
                        TaskStatus.COMPLETED, Priority.HIGH, 80,
                        LocalDate.now().minusDays(1), bob, alice),

                createTask("Fix pagination bug",
                        "Task list returns wrong page size when limit param is omitted",
                        TaskStatus.IN_PROGRESS, Priority.MEDIUM, 40,
                        LocalDate.now().plusDays(2), bob, alice),

                // ── Sara ──
                createTask("Build notification system",
                        "Send email alerts when a task due date is within 24 hours",
                        TaskStatus.IN_PROGRESS, Priority.HIGH, 110,
                        LocalDate.now().plusDays(5), sara, alice),

                createTask("Design achievements UI",
                        "Create mockups for the achievements and badge display page",
                        TaskStatus.COMPLETED, Priority.MEDIUM, 55,
                        LocalDate.now().minusDays(3), sara, alice),

                createTask("Leaderboard optimization",
                        "Cache leaderboard results to reduce database queries",
                        TaskStatus.PENDING, Priority.MEDIUM, 70,
                        LocalDate.now().plusDays(8), sara, alice),

                createTask("User profile page",
                        "Build profile page showing stats, level, and earned badges",
                        TaskStatus.COMPLETED, Priority.HIGH, 85,
                        LocalDate.now().minusDays(7), sara, alice),

                // ── Mike ──
                createTask("Fix responsive layout",
                        "Dashboard breaks on screens smaller than 768px",
                        TaskStatus.PENDING, Priority.MEDIUM, 35,
                        LocalDate.now().plusDays(4), mike, alice),

                createTask("Add dark mode toggle",
                        "Implement theme switcher that persists to localStorage",
                        TaskStatus.CANCELLED, Priority.LOW, 25,
                        LocalDate.now().minusDays(2), mike, alice),

                // ── Lisa ──
                createTask("Implement XP multiplier",
                        "Award bonus XP for completing urgent tasks before deadline",
                        TaskStatus.COMPLETED, Priority.HIGH, 95,
                        LocalDate.now().minusDays(4), lisa, alice),

                createTask("Weekly digest email",
                        "Send users a weekly summary of their points and completed tasks",
                        TaskStatus.COMPLETED, Priority.MEDIUM, 65,
                        LocalDate.now().minusDays(8), lisa, alice),

                createTask("Streak tracking feature",
                        "Track daily login streaks and award bonus points",
                        TaskStatus.IN_PROGRESS, Priority.HIGH, 100,
                        LocalDate.now().plusDays(3), lisa, alice),

                createTask("Admin dashboard metrics",
                        "Add charts showing task completion rates per user",
                        TaskStatus.COMPLETED, Priority.MEDIUM, 75,
                        LocalDate.now().minusDays(10), lisa, alice),

                // ── Tom ──
                createTask("Read onboarding docs",
                        "Review the project README and architecture overview",
                        TaskStatus.COMPLETED, Priority.LOW, 15,
                        LocalDate.now().minusDays(1), tom, alice),

                createTask("Fix typo in register form",
                        "Label says 'Emal' instead of 'Email' on the register page",
                        TaskStatus.PENDING, Priority.LOW, 10,
                        LocalDate.now().plusDays(2), tom, alice)
        );

        taskRepository.saveAll(tasks);
        System.out.println("✅ Loaded " + tasks.size() + " assigned tasks");
    }

    // ─────────────────────────────────────────────
    //  MISSION BOARD (unassigned tasks)
    // ─────────────────────────────────────────────
    private void loadUnassignedTasks() {
        if (taskRepository.findUnassignedTasks().size() > 0) {
            System.out.println("⚠️  Unassigned tasks already exist - skipping");
            return;
        }

        System.out.println("\n🎯 Loading Mission Board Tasks...");

        User alice = userRepository.findByUsername("alice_manager").orElse(null);
        if (alice == null) return;

        List<Task> missions = Arrays.asList(
                createTask("Build mobile app UI",
                        "Create React Native components for the task management mobile client",
                        TaskStatus.PENDING, Priority.HIGH, 100,
                        LocalDate.now().plusDays(7), null, alice),

                createTask("Database optimization",
                        "Add composite indexes to tasks table to improve query performance",
                        TaskStatus.PENDING, Priority.URGENT, 150,
                        LocalDate.now().plusDays(3), null, alice),

                createTask("Write user documentation",
                        "Create an end-user guide covering all features of the task manager",
                        TaskStatus.PENDING, Priority.MEDIUM, 50,
                        LocalDate.now().plusDays(10), null, alice),

                createTask("Implement OAuth login",
                        "Add Google SSO as an alternative authentication method",
                        TaskStatus.PENDING, Priority.HIGH, 180,
                        LocalDate.now().plusDays(6), null, alice),

                createTask("Export tasks to CSV",
                        "Allow managers to download a CSV export of all tasks and statuses",
                        TaskStatus.PENDING, Priority.MEDIUM, 70,
                        LocalDate.now().plusDays(9), null, alice),

                createTask("Onboard new team members",
                        "Create accounts and assign starter tasks for three new hires",
                        TaskStatus.PENDING, Priority.LOW, 30,
                        LocalDate.now().plusDays(5), null, alice),

                createTask("Migrate to PostgreSQL",
                        "Swap the MySQL datasource for PostgreSQL and verify all queries",
                        TaskStatus.PENDING, Priority.URGENT, 200,
                        LocalDate.now().plusDays(4), null, alice),

                createTask("Add task search",
                        "Implement full-text search across task titles and descriptions",
                        TaskStatus.PENDING, Priority.MEDIUM, 85,
                        LocalDate.now().plusDays(8), null, alice)
        );

        taskRepository.saveAll(missions);
        System.out.println("✅ Loaded " + missions.size() + " mission board tasks");
    }

    // ─────────────────────────────────────────────
    //  USER ACHIEVEMENTS
    // ─────────────────────────────────────────────
    private void loadUserAchievements() {
        if (userAchievementRepository.count() > 0) {
            System.out.println("⚠️  User achievements already exist - skipping");
            return;
        }

        System.out.println("\n🏆 Loading User Achievements...");

        User john  = userRepository.findByUsername("john_doe").orElse(null);
        User jane  = userRepository.findByUsername("jane_smith").orElse(null);
        User sara  = userRepository.findByUsername("sara_jones").orElse(null);
        User lisa  = userRepository.findByUsername("lisa_park").orElse(null);
        User tom   = userRepository.findByUsername("tom_harris").orElse(null);
        User alice = userRepository.findByUsername("alice_manager").orElse(null);

        Achievement firstStep       = achievementRepository.findByName("First Step").orElse(null);
        Achievement taskMaster      = achievementRepository.findByName("Task Master").orElse(null);
        Achievement pointCollector  = achievementRepository.findByName("Point Collector").orElse(null);
        Achievement pointHunter     = achievementRepository.findByName("Point Hunter").orElse(null);
        Achievement pointLegend     = achievementRepository.findByName("Point Legend").orElse(null);

        int count = 0;

        // John – First Step + Point Collector
        if (john != null && firstStep != null)
            count += award(john, firstStep, LocalDateTime.now().minusDays(10));
        if (john != null && pointCollector != null)
            count += award(john, pointCollector, LocalDateTime.now().minusDays(7));

        // Jane – First Step + Task Master + Point Hunter
        if (jane != null && firstStep != null)
            count += award(jane, firstStep, LocalDateTime.now().minusDays(14));
        if (jane != null && taskMaster != null)
            count += award(jane, taskMaster, LocalDateTime.now().minusDays(8));
        if (jane != null && pointHunter != null)
            count += award(jane, pointHunter, LocalDateTime.now().minusDays(4));

        // Sara – First Step + Point Collector + Point Hunter
        if (sara != null && firstStep != null)
            count += award(sara, firstStep, LocalDateTime.now().minusDays(12));
        if (sara != null && pointCollector != null)
            count += award(sara, pointCollector, LocalDateTime.now().minusDays(9));
        if (sara != null && pointHunter != null)
            count += award(sara, pointHunter, LocalDateTime.now().minusDays(3));

        // Lisa – everything up to Point Legend
        if (lisa != null && firstStep != null)
            count += award(lisa, firstStep, LocalDateTime.now().minusDays(20));
        if (lisa != null && taskMaster != null)
            count += award(lisa, taskMaster, LocalDateTime.now().minusDays(15));
        if (lisa != null && pointCollector != null)
            count += award(lisa, pointCollector, LocalDateTime.now().minusDays(12));
        if (lisa != null && pointHunter != null)
            count += award(lisa, pointHunter, LocalDateTime.now().minusDays(6));
        if (lisa != null && pointLegend != null)
            count += award(lisa, pointLegend, LocalDateTime.now().minusDays(1));

        // Tom – just First Step
        if (tom != null && firstStep != null)
            count += award(tom, firstStep, LocalDateTime.now().minusDays(1));

        // Alice (manager) – Point Collector + Point Hunter
        if (alice != null && pointCollector != null)
            count += award(alice, pointCollector, LocalDateTime.now().minusDays(30));
        if (alice != null && pointHunter != null)
            count += award(alice, pointHunter, LocalDateTime.now().minusDays(20));

        System.out.println("✅ Loaded " + count + " user-achievement relationships");
    }

    // ─────────────────────────────────────────────
    //  HELPER METHODS
    // ─────────────────────────────────────────────
    private int award(User user, Achievement achievement, LocalDateTime earnedAt) {
        UserAchievement ua = new UserAchievement();
        ua.setUser(user);
        ua.setAchievement(achievement);
        ua.setEarnedAt(earnedAt);
        userAchievementRepository.save(ua);
        return 1;
    }

    private Achievement createAchievement(String name, String description,
                                          AchievementCriteria criteria, int value, String icon) {
        Achievement a = new Achievement();
        a.setName(name);
        a.setDescription(description);
        a.setCriteriaType(criteria);
        a.setCriteriaValue(value);
        a.setBadgeIcon(icon);
        return a;
    }

    private User createUser(String username, String email, String rawPassword,
                            String name, Role role, int totalPoints, int level) {
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setName(name);
        u.setRole(role);
        u.setTotalPoints(totalPoints);
        u.setLevel(level);
        return u;
    }

    private Task createTask(String title, String description, TaskStatus status,
                            Priority priority, int points, LocalDate dueDate,
                            User assignedTo, User createdBy) {
        Task t = new Task();
        t.setTitle(title);
        t.setDescription(description);
        t.setStatus(status);
        t.setPriority(priority);
        t.setPoints(points);
        t.setDueDate(dueDate);
        t.setAssignedTo(assignedTo);
        t.setCreatedBy(createdBy);
        if (status == TaskStatus.COMPLETED) {
            t.setCompletedAt(LocalDateTime.now().minusDays(1));
        }
        return t;
    }
}