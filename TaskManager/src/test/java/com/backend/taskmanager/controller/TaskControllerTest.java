package com.backend.taskmanager.controller;

import com.backend.taskmanager.dto.TaskRequest;
import com.backend.taskmanager.entity.*;
import com.backend.taskmanager.service.TaskService;
import com.backend.taskmanager.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;

    private Task testTask;
    private User testUser;
    private User creator;
    private TaskRequest createTaskRequest;
    private TaskRequest updateTaskRequest;
    private TaskRequest updateMissingTaskRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setRole(Role.EMPLOYEE);

        creator = new User();
        creator.setId(2L);
        creator.setUsername("creator");
        creator.setRole(Role.MANAGER);

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setStatus(TaskStatus.PENDING);
        testTask.setPriority(Priority.MEDIUM);
        testTask.setPoints(50);
        testTask.setDueDate(LocalDate.now().plusDays(7));
        testTask.setAssignedTo(testUser);
        testTask.setCreatedBy(creator);

        createTaskRequest = new TaskRequest();
        createTaskRequest.setTitle("New Task");
        createTaskRequest.setDescription("New Description");
        createTaskRequest.setPriority(Priority.HIGH);
        createTaskRequest.setPoints(100);
        createTaskRequest.setDueDate(LocalDate.now().plusDays(3));
        createTaskRequest.setAssignedToId(testUser.getId());
        createTaskRequest.setCreatedById(creator.getId());

        updateTaskRequest = new TaskRequest();
        updateTaskRequest.setTitle("Updated Title");
        updateTaskRequest.setDescription("Updated Description");

        updateMissingTaskRequest = new TaskRequest();
        updateMissingTaskRequest.setTitle("Missing Task");
        updateMissingTaskRequest.setAssignedToId(testUser.getId());
    }

    // ===== GET Endpoint Tests =====

    @Test
    void getAllTasks_ShouldReturnAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(testTask));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists() throws Exception {
        when(taskService.findById(1L)).thenReturn(Optional.of(testTask));

        mockMvc.perform(get("/api/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getTaskById_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        when(taskService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTasksByUser_ShouldReturnUserTasks() throws Exception {
        when(taskService.findByAssignedToId(1L)).thenReturn(Arrays.asList(testTask));

        mockMvc.perform(get("/api/tasks/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].assignedTo.id").value(1));
    }

    @Test
    void getActiveTasksByUser_ShouldReturnActiveTasks() throws Exception {
        when(taskService.findActiveTasksByUser(1L)).thenReturn(Arrays.asList(testTask));

        mockMvc.perform(get("/api/tasks/user/{userId}/active", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getTasksByUserAndStatus_ShouldReturnFilteredTasks() throws Exception {
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskService.findByAssignedToAndStatus(eq(testUser), eq(TaskStatus.PENDING)))
                .thenReturn(Arrays.asList(testTask));

        mockMvc.perform(get("/api/tasks/user/{userId}/status/{status}", 1L, "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void countTasksByUserAndStatus_ShouldReturnCount() throws Exception {
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskService.countByAssignedToAndStatus(testUser, TaskStatus.COMPLETED)).thenReturn(5L);

        mockMvc.perform(get("/api/tasks/user/{userId}/count/status/{status}", 1L, "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void getTasksByStatus_ShouldReturnTasksByStatus() throws Exception {
        when(taskService.findByStatus(TaskStatus.PENDING)).thenReturn(Arrays.asList(testTask));

        mockMvc.perform(get("/api/tasks/status/{status}", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getTasksByPriority_ShouldReturnTasksByPriority() throws Exception {
        when(taskService.findByPriority(Priority.HIGH)).thenReturn(Arrays.asList(testTask));

        mockMvc.perform(get("/api/tasks/priority/{priority}", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getTasksByStatusAndPriority_ShouldReturnFilteredTasks() throws Exception {
        when(taskService.findByStatusAndPriority(TaskStatus.PENDING, Priority.HIGH))
                .thenReturn(Arrays.asList(testTask));

        mockMvc.perform(get("/api/tasks/filter")
                        .param("status", "PENDING")
                        .param("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getTasksByStatusOnly_ShouldReturnTasks() throws Exception {
        when(taskService.findByStatus(TaskStatus.PENDING)).thenReturn(Arrays.asList(testTask));

        mockMvc.perform(get("/api/tasks/filter")
                        .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getTasksByPriorityOnly_ShouldReturnTasks() throws Exception {
        when(taskService.findByPriority(Priority.HIGH)).thenReturn(Arrays.asList(testTask));

        mockMvc.perform(get("/api/tasks/filter")
                        .param("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getTasksWithNoFilters_ShouldReturnAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(testTask));

        mockMvc.perform(get("/api/tasks/filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // ===== POST Endpoint Tests =====

    @Test
    void createTask_ShouldCreateAndReturnTask() throws Exception {
        when(taskService.createTask(any(Task.class))).thenReturn(testTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.id").value(1));
    }

    // ===== PUT Endpoint Tests =====

    @Test
    void updateTask_ShouldUpdateAndReturnTask() throws Exception {
        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(testTask);

        mockMvc.perform(put("/api/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTaskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void updateTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        when(taskService.updateTask(eq(99L), any(Task.class)))
                .thenThrow(new RuntimeException("Task not found"));

        mockMvc.perform(put("/api/tasks/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMissingTaskRequest)))
                .andExpect(status().isNotFound());
    }

    // ===== DELETE Endpoint Tests =====

    @Test
    void deleteTask_ShouldReturnNoContent() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    // ===== PATCH Endpoint Tests =====

    @Test
    void assignTask_ShouldAssignTaskToUser() throws Exception {
        when(taskService.assignTask(1L, 1L)).thenReturn(testTask);

        mockMvc.perform(patch("/api/tasks/{id}/assign", 1L)
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignedTo.id").value(1));
    }

    @Test
    void assignTask_ShouldReturnNotFound_WhenTaskNotFound() throws Exception {
        when(taskService.assignTask(99L, 1L)).thenThrow(new RuntimeException("Task not found"));

        mockMvc.perform(patch("/api/tasks/{id}/assign", 99L)
                        .param("userId", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void completeTask_ShouldCompleteTaskAndAwardPoints() throws Exception {
        Task completedTask = new Task();
        completedTask.setId(1L);
        completedTask.setTitle("Completed Task");
        completedTask.setStatus(TaskStatus.COMPLETED);
        completedTask.setPoints(50);

        when(taskService.completeTask(1L, 1L)).thenReturn(completedTask);

        mockMvc.perform(patch("/api/tasks/{id}/complete", 1L)
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void completeTask_ShouldReturnBadRequest_WhenTaskAlreadyCompleted() throws Exception {
        when(taskService.completeTask(1L, 1L)).thenThrow(new RuntimeException("Task already completed"));

        mockMvc.perform(patch("/api/tasks/{id}/complete", 1L)
                        .param("userId", "1"))
                .andExpect(status().isBadRequest());
    }
}
