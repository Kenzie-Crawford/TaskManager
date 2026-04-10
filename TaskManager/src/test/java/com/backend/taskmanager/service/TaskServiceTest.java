package com.backend.taskmanager.service;

import com.backend.taskmanager.entity.*;
import com.backend.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task testTask;
    private User testUser;
    private User creator;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        creator = new User();
        creator.setId(2L);
        creator.setUsername("creator");

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
    }

    @Test
    void findById_ShouldReturnTask_WhenTaskExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        Optional<Task> result = taskService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Task", result.get().getTitle());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenTaskDoesNotExist() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.findById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void createTask_ShouldSaveAndReturnTask() {
        // Mock both creator and assignee lookups
        when(userService.findById(2L)).thenReturn(Optional.of(creator));
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task result = taskService.createTask(testTask);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void createTask_ShouldThrowException_WhenCreatorNotFound() {
        Task taskWithInvalidCreator = new Task();
        User invalidCreator = new User();
        invalidCreator.setId(99L);
        taskWithInvalidCreator.setCreatedBy(invalidCreator);

        User assignee = new User();
        assignee.setId(1L);
        taskWithInvalidCreator.setAssignedTo(assignee);

        when(userService.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.createTask(taskWithInvalidCreator));

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void completeTask_ShouldAwardPoints_WhenTaskCompleted() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.completeTask(1L, 1L)).thenReturn(1);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(userService.addPoints(1L, 50)).thenReturn(testUser);

        Task result = taskService.completeTask(1L, 1L);

        assertNotNull(result);
        verify(userService).addPoints(1L, 50);
        verify(taskRepository).completeTask(1L, 1L);
    }

    @Test
    void completeTask_ShouldThrowException_WhenTaskNotAssignedToUser() {
        testTask.setAssignedTo(null);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        assertThrows(RuntimeException.class, () -> taskService.completeTask(1L, 1L));
    }

    @Test
    void assignTask_ShouldAssignUserToTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task result = taskService.assignTask(1L, 1L);

        assertNotNull(result);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void deleteTask_ShouldDelete_WhenTaskExists() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteTask_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> taskService.deleteTask(99L));
    }
}