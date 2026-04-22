# API Documentation

Base URL: `http://localhost:8081/api`

All endpoints except `/auth/login` and `/auth/register` require a valid JWT token in the `Authorization` header:

```
Authorization: Bearer <token>
```

---

## Auth

### POST `/auth/register`
Register a new user account.

**Request Body**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "name": "John Doe"
}
```

**Response `201 Created`**
```json
{
  "success": true,
  "message": "Registration successful! Please login.",
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "name": "John Doe",
  "role": "EMPLOYEE"
}
```

---

### POST `/auth/login`
Authenticate and receive a JWT token.

**Request Body**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**Response `200 OK`**
```json
{
  "success": true,
  "token": "<jwt_token>",
  "tokenType": "Bearer",
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "name": "John Doe",
  "role": "EMPLOYEE",
  "totalPoints": 480,
  "level": 5
}
```

---

### GET `/auth/me`
Get the currently authenticated user's profile.

**Response `200 OK`**
```json
{
  "id": 1,
  "username": "john_doe",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "EMPLOYEE",
  "totalPoints": 480,
  "level": 5
}
```

---

### POST `/auth/logout`
Invalidate the current session.

**Response `200 OK`**
```json
{ "message": "Logged out" }
```

---

## Tasks

### GET `/tasks`
Get all tasks in the system.

**Response `200 OK`** — Array of Task objects

---

### GET `/tasks/{id}`
Get a single task by ID.

**Response `200 OK`** — Task object
**Response `404 Not Found`**

---

### POST `/tasks`
Create a new task. **Requires MANAGER or ADMIN role.**

**Request Body**
```json
{
  "title": "Fix login bug",
  "description": "Authentication failing on mobile",
  "status": "PENDING",
  "priority": "URGENT",
  "points": 100,
  "dueDate": "2026-04-25",
  "assignedToId": 1,
  "createdById": 8
}
```

**Response `200 OK`** — Created Task object

---

### PUT `/tasks/{id}`
Update an existing task. **Requires MANAGER or ADMIN role.**

**Request Body** — Same shape as POST `/tasks`

**Response `200 OK`** — Updated Task object
**Response `404 Not Found`**

---

### DELETE `/tasks/{id}`
Delete a task. **Requires MANAGER or ADMIN role.**

**Response `204 No Content`**

---

### GET `/tasks/user/{userId}`
Get all tasks assigned to a specific user.

**Response `200 OK`** — Array of Task objects

---

### GET `/tasks/user/{userId}/active`
Get all non-completed tasks assigned to a user.

**Response `200 OK`** — Array of Task objects

---

### GET `/tasks/user/{userId}/status/{status}`
Get tasks for a user filtered by status.

**Path Params** — `status`: `PENDING` | `IN_PROGRESS` | `COMPLETED` | `CANCELLED`

**Response `200 OK`** — Array of Task objects

---

### GET `/tasks/status/{status}`
Get all tasks with a given status.

**Response `200 OK`** — Array of Task objects

---

### GET `/tasks/priority/{priority}`
Get all tasks with a given priority.

**Path Params** — `priority`: `LOW` | `MEDIUM` | `HIGH` | `URGENT`

**Response `200 OK`** — Array of Task objects

---

### GET `/tasks/filter`
Filter tasks by status and/or priority.

**Query Params** — `status` (optional), `priority` (optional)

**Response `200 OK`** — Array of Task objects

---

### PATCH `/tasks/{id}/assign`
Assign a task to a user. **Requires MANAGER or ADMIN role.**

**Query Params** — `userId`

**Response `200 OK`** — Updated Task object

---

### PATCH `/tasks/{id}/complete`
Mark a task as completed and award points to the assigned user.

**Query Params** — `userId`

**Response `200 OK`** — Completed Task object
**Response `400 Bad Request`**

---

### GET `/tasks/mission-board`
Get all unassigned tasks available on the mission board.

**Response `200 OK`** — Array of Task objects

---

### PATCH `/tasks/mission-board/{taskId}/claim`
Claim an unassigned task from the mission board.

**Query Params** — `userId`

**Response `200 OK`** — Claimed Task object
**Response `400 Bad Request`**

---

## Users

### GET `/users`
Get all users.

**Response `200 OK`** — Array of User objects

---

### GET `/users/{id}`
Get a user by ID.

**Response `200 OK`** — User object
**Response `404 Not Found`**

---

### GET `/users/username/{username}`
Get a user by username.

**Response `200 OK`** — User object

---

### GET `/users/email/{email}`
Get a user by email address.

**Response `200 OK`** — User object

---

### GET `/users/role/{role}`
Get all users with a given role.

**Path Params** — `role`: `EMPLOYEE` | `MANAGER` | `ADMIN`

**Response `200 OK`** — Array of User objects

---

### GET `/users/leaderboard`
Get users ranked by total points.

**Query Params** — `limit` (optional, default 20)

**Response `200 OK`**
```json
[
  {
    "rank": 1,
    "userId": 6,
    "username": "lisa_park",
    "name": "Lisa Park",
    "totalPoints": 970,
    "level": 10,
    "achievementCount": 5
  }
]
```

---

### PUT `/users/{id}`
Update a user's profile details.

**Response `200 OK`** — Updated User object

---

### PUT `/users/{id}/points`
Add points to a user.

**Query Params** — `points`

**Response `200 OK`** — Updated User object

---

### DELETE `/users/{id}`
Delete a user by ID.

**Response `204 No Content`**

---

## Achievements

### GET `/achievements`
Get all achievements in the system.

**Response `200 OK`** — Array of Achievement objects

---

### GET `/achievements/{id}`
Get a single achievement by ID.

**Response `200 OK`** — Achievement object
**Response `404 Not Found`**

---

### GET `/achievements/user/{userId}`
Get all achievements earned by a user.

**Response `200 OK`**
```json
[
  {
    "id": 1,
    "userId": 2,
    "username": "jane_smith",
    "achievementId": 2,
    "achievementName": "Task Master",
    "achievementDescription": "Complete 10 tasks",
    "badgeIcon": "🏆",
    "criteriaType": "TASKS_COMPLETED",
    "criteriaValue": 10,
    "earnedAt": "2026-04-14T10:30:00"
  }
]
```

---

### GET `/achievements/user/{userId}/available`
Get achievements the user has not yet earned.

**Response `200 OK`** — Array of Achievement objects

---

### GET `/achievements/user/{userId}/count`
Get the count of achievements earned by a user.

**Response `200 OK`** — Integer

---

### GET `/achievements/user/{userId}/has/{achievementId}`
Check if a user has earned a specific achievement.

**Response `200 OK`** — Boolean

---

### GET `/achievements/user/{userId}/progress/{achievementId}`
Get a user's progress toward a specific achievement as a percentage (0–100).

**Response `200 OK`** — Integer

---

### POST `/achievements/user/{userId}/check`
Trigger an achievement check for a user — awards any newly-qualifying achievements.

**Response `200 OK`** — Confirmation string

---

## Data Models

### Task
| Field         | Type        | Description                              |
|---------------|-------------|------------------------------------------|
| id            | Long        | Auto-generated ID                        |
| title         | String      | Task title                               |
| description   | String      | Task description                         |
| status        | Enum        | PENDING, IN_PROGRESS, COMPLETED, CANCELLED |
| priority      | Enum        | LOW, MEDIUM, HIGH, URGENT                |
| points        | Integer     | XP awarded on completion                 |
| dueDate       | LocalDate   | Task deadline                            |
| assignedTo    | User        | Assigned user (null = mission board)     |
| createdBy     | User        | User who created the task                |
| completedAt   | DateTime    | When the task was completed              |
| createdAt     | DateTime    | Record creation timestamp                |

### User
| Field         | Type        | Description                              |
|---------------|-------------|------------------------------------------|
| id            | Long        | Auto-generated ID                        |
| username      | String      | Unique login username                    |
| email         | String      | Unique email address                     |
| name          | String      | Display name                             |
| role          | Enum        | EMPLOYEE, MANAGER, ADMIN                 |
| totalPoints   | Integer     | Cumulative XP earned                     |
| level         | Integer     | Calculated as (totalPoints / 100) + 1    |

### Achievement
| Field          | Type    | Description                                              |
|----------------|---------|----------------------------------------------------------|
| id             | Long    | Auto-generated ID                                        |
| name           | String  | Achievement name                                         |
| description    | String  | What's required to earn it                              |
| criteriaType   | Enum    | TASKS_COMPLETED, TOTAL_POINTS, ACHIEVEMENTS_COUNT        |
| criteriaValue  | Integer | Threshold value to unlock                                |
| badgeIcon      | String  | Emoji icon displayed in UI                               |