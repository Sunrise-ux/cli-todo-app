package com.todoapp.repository;

import com.todoapp.model.Task;
import com.todoapp.exception.TaskNotFoundException;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskRepository {
    private Map<Integer, Task> tasks = new HashMap<>();
    private AtomicInteger idCounter = new AtomicInteger(1);
    private static final String DATA_FILE = "todo-data.ser";
    
    public TaskRepository() {
        loadFromFile();
    }
    
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idCounter.getAndIncrement());
        } else {
            // Если задача с таким ID уже существует, обновляем счетчик
            if (task.getId() >= idCounter.get()) {
                idCounter.set(task.getId() + 1);
            }
        }
        tasks.put(task.getId(), task);
        saveToFile();
        return task;
    }
    
    public Optional<Task> findById(Integer id) {
        return Optional.ofNullable(tasks.get(id));
    }
    
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }
    
    public List<Task> findByCompleted(boolean completed) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.isCompleted() == completed) {
                result.add(task);
            }
        }
        return result;
    }
    
    public void delete(Integer id) throws TaskNotFoundException {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }
        tasks.remove(id);
        saveToFile();
    }
    
    public boolean exists(Integer id) {
        return tasks.containsKey(id);
    }
    
    public long count() {
        return tasks.size();
    }
    
    public void clear() {
        tasks.clear();
        idCounter.set(1);
        saveToFile();
    }
    
    // Вспомогательный класс для сериализации всего состояния
    private static class RepositoryState implements Serializable {
        private static final long serialVersionUID = 1L;
        Map<Integer, Task> tasks;
        int nextId;
    }
    
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            // Файла нет - это первый запуск, ничего не загружаем
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            RepositoryState state = (RepositoryState) ois.readObject();
            this.tasks = state.tasks;
            this.idCounter.set(state.nextId);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Warning: Could not load data: " + e.getMessage());
            // В случае ошибки начинаем с чистого листа
            tasks = new HashMap<>();
            idCounter.set(1);
        }
    }
    
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            RepositoryState state = new RepositoryState();
            state.tasks = this.tasks;
            state.nextId = this.idCounter.get();
            oos.writeObject(state);
        } catch (IOException e) {
            System.err.println("Error: Could not save data: " + e.getMessage());
        }
    }
}