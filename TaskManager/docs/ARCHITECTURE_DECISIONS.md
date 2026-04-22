Architecture Decision Records
This document captures the key technical decisions made during the design and development of WorkFlow, along with the reasoning behind each choice.

ADR-001: JWT for Authentication
Decision: Use stateless JWT-based authentication instead of session-based authentication.
Context:
The application needed a secure authentication mechanism that would work cleanly with a React SPA frontend calling a separate Spring Boot API on a different port.
Reasoning:
Session-based auth requires the server to maintain session state, which complicates scaling and requires sticky sessions or a shared session store. JWT tokens are self-contained — the server validates the token on each request without storing session state, which aligns with REST's stateless constraint.
The frontend stores the token in localStorage and attaches it to every request via an Axios interceptor, removing the need to pass credentials manually on each call.
Trade-offs:
JWT tokens cannot be invalidated before expiry without a token blacklist. For this application's scope, the 24-hour expiry window was deemed acceptable.

ADR-002: Role-Based Access Control with Three Roles
Decision: Implement three roles — EMPLOYEE, MANAGER, ADMIN — enforced at both the backend (@PreAuthorize) and frontend (conditional rendering).
Context:
The system needed to support self-service task claiming by employees while restricting task creation, editing, and deletion to authorized users.
Reasoning:
Three roles cover the real-world hierarchy: employees do work, managers assign and oversee work, admins have full access. Spring Security's @PreAuthorize("hasAnyRole('MANAGER','ADMIN')") annotation enforces this at the API level, ensuring that even if the frontend is bypassed, unauthorized operations are rejected at the server.
Frontend role checks (reading from localStorage) are used purely for UI convenience — showing or hiding buttons — not as a security boundary.

ADR-003: Gamification via Points, Levels, and Achievements
Decision: Award points on task completion, derive level from total points, and use a configurable achievement system with a separate achievements table.
Context:
The core business requirement was to increase task completion motivation through gamification mechanics common in productivity and RPG applications.
Reasoning:
Keeping points on the User entity (totalPoints, level) allows leaderboard queries to be a simple sort with no joins. Level is derived — (totalPoints / 100) + 1 — rather than stored independently, avoiding sync issues.
Achievements are stored as their own entities with a criteriaType enum and criteriaValue integer. This makes adding new achievement types a data change rather than a code change. Progress checking is handled in AchievementServiceImpl which evaluates each criterion type at runtime.

ADR-004: Mission Board for Self-Service Task Claiming
Decision: Unassigned tasks (assignedTo = null) form a shared mission board that any authenticated employee can claim.
Context:
A strict top-down assignment model (manager assigns every task to a specific person) reduces employee autonomy. A self-service model increases ownership and engagement, fitting the gamification theme.
Reasoning:
Storing unassigned tasks as assignedTo = null requires no schema changes — just a filtered query (findUnassignedTasks) in the repository. Claiming a task atomically sets assignedTo to the requesting user and changes status to IN_PROGRESS, preventing two users from claiming the same task simultaneously.

ADR-005: Service Interface + Implementation Pattern
Decision: All services are defined as Java interfaces (TaskService, UserService, etc.) with separate Impl classes containing the business logic.
Context:
Standard Spring Boot project architecture decision made at the start of development.
Reasoning:
The interface/implementation separation enforces the Dependency Inversion Principle — controllers depend on the TaskService abstraction, not the concrete TaskServiceImpl. This makes unit testing straightforward since service dependencies can be mocked at the interface level with Mockito. It also provides a clear contract for what each service exposes without exposing implementation details.

ADR-006: DataLoader for Seed Data
Decision: Use a CommandLineRunner (DataLoader) to populate the database with demo users, tasks, and achievements on startup.
Context:
A demo application needs realistic data to showcase all features during a presentation. Manually entering data through the UI before every demo is error-prone and time-consuming.
Reasoning:
CommandLineRunner runs once after the Spring context is fully initialized, ensuring all beans (repositories, password encoder) are available. Each load* method guards against re-seeding with a repository.count() > 0 check, so the loader is safe to leave enabled in validate mode — it simply skips if data exists.
Combined with ddl-auto=create-drop, the entire database can be wiped and reseeded to a known state by restarting the server.

ADR-007: Scheduled Monthly Reset
Decision: Use Spring's @Scheduled with a cron expression to reset all user points and achievements on the 1st of each month.
Context:
A persistent leaderboard where early adopters accumulate insurmountable point leads reduces competition. Monthly resets keep the leaderboard competitive and give all employees a fresh start each cycle.
Reasoning:
Spring's @Scheduled annotation with @EnableScheduling on the main application class requires no external scheduler infrastructure. The cron expression 0 0 0 21 * * (midnight on the 21st) runs server-side and updates the database directly. The frontend reflects the reset on the next page load or data fetch — no real-time push is needed for a once-monthly event.
Two separate scheduled methods handle the reset: one in UserServiceImpl (zeroes points) and one in AchievementServiceImpl (clears user-achievement records), keeping the reset logic co-located with the services that own that data.

ADR-008: Axios with Request Interceptor
Decision: Use Axios instead of the native Fetch API, with a single interceptor that attaches the JWT token to every outgoing request.
Context:
Every API call except login/register requires the Authorization: Bearer <token> header. Manually attaching this to every fetch() call would be repetitive and error-prone.
Reasoning:
Axios interceptors provide a single place to attach the token, handle auth errors (e.g., redirect to login on 401), and configure the base URL. This means every service module (taskService.js, achievementService.js) can make clean calls without any auth boilerplate. Axios also automatically parses JSON responses, reducing the two-step fetch().then(r => r.json()) pattern to a single awaited call.