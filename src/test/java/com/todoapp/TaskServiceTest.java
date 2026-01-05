package com.todoapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.todoapp.service.TaskService;
import com.todoapp.repository.TaskRepository;
import com.todoapp.model.Task;
import com.todoapp.exception.TaskNotFoundException;
import com.todoapp.model.Task.Priority;
import java.util.List;

public class TaskServiceTest {
    private TaskService taskService;
    
    @BeforeEach
    public void setUp() {
        // Используем временный файл для тестов
        TaskRepository repository = new TaskRepository("test-tasks.ser");
        taskService = new TaskService(repository);
    }
    
    @Test
    public void testAddTask() {
        Task task = taskService.addTask("Test Task", "Test Description", Priority.MEDIUM);
        
        Assertions.assertNotNull(task.getId());
        Assertions.assertEquals("Test Task", task.getTitle());
        Assertions.assertEquals("Test Description", task.getDescription());
        Assertions.assertEquals(Priority.MEDIUM, task.getPriority());
        Assertions.assertFalse(task.isCompleted());
    }
    
    @Test
    public void testGetAllTasks() {
        taskService.addTask("Task 1", "Desc 1", Priority.LOW);
        taskService.addTask("Task 2", "Desc 2", Priority.HIGH);
        
        List<Task> tasks = taskService.getAllTasks();
        Assertions.assertEquals(2, tasks.size());
    }
    
    @Test 
    public void testCompleteTask() {
        Task task = taskService.addTask("Complete me", "", Priority.MEDIUM);
        
        taskService.completeTask(task.getId());
        Task completed = taskService.getTaskById(task.getId())
            .orElseThrow(() -> new RuntimeException("Task not found"));
        
        Assertions.assertTrue(completed.isCompleted());
        Assertions.assertNotNull(completed.getCompletedAt());
    }
    
    @Test
    public void testDeleteTask() {
        Task task = taskService.addTask("Delete me", "", Priority.LOW);
        
        boolean deleted = taskService.deleteTask(task.getId());
        Assertions.assertTrue(deleted);
        
        // Проверяем что задача удалена
        Assertions.assertThrows(TaskNotFoundException.class, 
            () -> taskService.getTaskById(task.getId()));
    }
    
    @Test
    public void testTaskNotFound() {
        Assertions.assertThrows(TaskNotFoundException.class, 
            () -> taskService.getTaskById(999));
    }
}