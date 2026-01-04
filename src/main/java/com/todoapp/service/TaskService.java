package com.todoapp.service;

import com.todoapp.model.Task;
import com.todoapp.repository.TaskRepository;
import com.todoapp.exception.TaskNotFoundException;
import java.util.List;
import java.util.Optional;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task createTask(String title, String description, Task.Priority priority) {
        Task task = new Task(title, description);
        task.setPriority(priority);
        return repository.save(task);
    }

    public Optional<Task> getTask(Integer id) {
        return repository.findById(id);
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public List<Task> getActiveTasks() {
        return repository.findByCompleted(false);
    }

    public List<Task> getCompletedTasks() {
        return repository.findByCompleted(true);
    }

    public Task updateTask(Integer id, String title, String description, Task.Priority priority) 
            throws TaskNotFoundException {
        Task task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        
        if (title != null) {
            task.setTitle(title);
        }
        if (description != null) {
            task.setDescription(description);
        }
        if (priority != null) {
            task.setPriority(priority);
        }
        
        return repository.save(task);
    }

    public Task markAsCompleted(Integer id) throws TaskNotFoundException {
        Task task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        task.setCompleted(true);
        return repository.save(task);
    }

    public void deleteTask(Integer id) throws TaskNotFoundException {
        repository.delete(id);
    }

    public long getTaskCount() {
        return repository.count();
    }

    public long getActiveTaskCount() {
        return repository.findByCompleted(false).size();
    }

    public long getCompletedTaskCount() {
        return repository.findByCompleted(true).size();
    }
}