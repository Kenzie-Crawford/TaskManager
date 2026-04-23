# WorkFlow — Gamified Task Management System

WorkFlow is a full-stack web application that transforms everyday workplace tasks into a gamified experience. Employees earn XP, level up, unlock achievements, and compete on a leaderboard — all while getting real work done.

Built with **React + Vite** on the frontend and **Spring Boot + MySQL** on the backend, secured with **JWT authentication** and **role-based access control**.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Default Accounts](#default-accounts)
- [Environment Variables](#environment-variables)
- [Running the App](#running-the-app)

---

## Features

- **JWT Authentication** — Secure login and registration with token-based auth
- **Role-Based Access Control** — Three roles: EMPLOYEE, MANAGER, ADMIN
- **Mission Board** — Employees can browse and self-claim unassigned tasks
- **XP & Leveling** — Completing tasks awards points; every 100 points = 1 level
- **Achievements** — Configurable badge system with progress tracking
- **Leaderboard** — Real-time ranking of all users by total points
- **Monthly Reset** — Points and achievements reset automatically on the 1st of each month via a scheduled job
- **Admin Panel** — Managers and admins can create, edit, assign, and delete tasks

---

## Tech Stack

| Layer        | Technology                          |
|--------------|-------------------------------------|
| Frontend     | React 18, Vite, React Router v6     |
| HTTP Client  | Axios                               |
| Backend      | Spring Boot 3.2, Java 21            |
| Security     | Spring Security, JWT (jjwt 0.11.5)  |
| ORM          | Spring Data JPA / Hibernate         |
| Database     | MySQL 8                             |
| Build Tool   | Maven                               |
| Other        | Lombok, BCrypt                      |

---

## Project Structure

```
TaskManager/
├── frontend/                   # React + Vite frontend
│   └── src/
│       ├── Components/         # Reusable UI components
│       ├── Pages/              # Page-level components
│       ├── Services/           # Axios API service modules
│       └── App.jsx             # Router and app entry point
│
└── TaskManager/                # Spring Boot backend
    └── src/main/java/com/backend/taskmanager/
        ├── config/             # DataLoader seed data
        ├── controller/         # REST controllers
        ├── dto/                # Request/Response DTOs
        ├── entity/             # JPA entities
        ├── repository/         # Spring Data repositories
        ├── security/           # JWT filter, config, providers
        └── service/            # Business logic (interface + impl)
```

---

## Getting Started

### Prerequisites

- Java 21
- Maven 3.8+
- MySQL 8
- Node.js 18+ and npm

### Database Setup

1. Create the database:
```sql
CREATE DATABASE task_manager;
```

2. Update credentials in `TaskManager/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_manager
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. For a fresh seed on first run, set:
```properties
spring.jpa.hibernate.ddl-auto=create-drop
```
Switch back to `validate` after first startup to preserve data between restarts.

---

## Default Accounts

These accounts are created automatically by the `DataLoader` on first startup.

| Username        | Password     | Role     |
|-----------------|--------------|----------|
| john_doe        | password123  | EMPLOYEE |
| jane_smith      | password123  | EMPLOYEE |
| bob_wilson      | password123  | EMPLOYEE |
| sara_jones      | password123  | EMPLOYEE |
| mike_chen       | password123  | EMPLOYEE |
| lisa_park       | password123  | EMPLOYEE |
| tom_harris      | password123  | EMPLOYEE |
| alice_manager   | password123  | MANAGER  |
| admin_user      | admin123     | ADMIN    |

---

## Environment Variables

### Backend (`application.properties`)

| Property                        | Description                        |
|---------------------------------|------------------------------------|
| `spring.datasource.url`         | MySQL connection URL                |
| `spring.datasource.username`    | Database username                  |
| `spring.datasource.password`    | Database password                  |
| `jwt.secret`                    | Secret key for signing JWT tokens  |
| `jwt.expiration`                | Token expiry in milliseconds       |
| `server.port`                   | Backend server port (default 8081) |

### Frontend (`src/Services/api.js`)

The base URL defaults to `http://localhost:8081/api`. Update this if your backend runs on a different port.

---

## Running the App

### Backend

```bash
cd TaskManager/TaskManager
./mvnw spring:boot-run
```

The API will be available at `http://localhost:8081`.

### Frontend

```bash
cd TaskManager/frontend
npm install
npm run dev
```

The app will be available at `http://localhost:5173`.
