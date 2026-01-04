package com.todoapp;

import com.todoapp.model.Task;
import com.todoapp.repository.TaskRepository;
import com.todoapp.service.TaskService;
import com.todoapp.exception.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    private TaskService taskService;
    private TaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TaskRepository();
        taskService = new TaskService(repository);
    }

    @Test
    void testCreateTask() {
        Task task = taskService.createTask("Test Task", "Test Description", Task.Priority.HIGH);
        
        assertNotNull(task.getId());
        assertEquals("Test Task", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals(Task.Priority.HIGH, task.getPriority());
        assertFalse(task.isCompleted());
    }

    @Test
    void testGetAllTasks() {
        taskService.createTask("Task 1", "Desc 1", Task.Priority.LOW);
        taskService.createTask("Task 2", "Desc 2", Task.Priority.MEDIUM);
        
        assertEquals(2, taskService.getAllTasks().size());
    }

    @Test
    void testMarkAsCompleted() throws TaskNotFoundException {
        Task task = taskService.createTask("Test Task", "Test Description", Task.Priority.MEDIUM);
        Task completed = taskService.markAsCompleted(task.getId());
        
        assertTrue(completed.isCompleted());
        assertNotNull(completed.getCompletedAt());
    }

    @Test
    void testDeleteTask() throws TaskNotFoundException {
        Task task = taskService.createTask("Test Task", "Test Description", Task.Priority.MEDIUM);
        taskService.deleteTask(task.getId());
        
        assertTrue(taskService.getAllTasks().isEmpty());
    }

    @Test
    void testTaskNotFoundException() {
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.markAsCompleted(999);
        });
    }

    @Test
    void testTaskCounts() {
        taskService.createTask("Task 1", "Desc 1", Task.Priority.LOW);
        taskService.createTask("Task 2", "Desc 2", Task.Priority.MEDIUM);
        
        assertEquals(2, taskService.getTaskCount());
        assertEquals(2, taskService.getActiveTaskCount());
        assertEquals(0, taskService.getCompletedTaskCount());
    }
}