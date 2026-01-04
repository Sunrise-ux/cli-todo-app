package com.todoapp.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import com.todoapp.model.Task;
import com.todoapp.service.TaskService;
import java.util.concurrent.Callable;

@Command(name = "add", description = "Add a new task", mixinStandardHelpOptions = true)
class AddCommand implements Callable<Integer> {
    
    @Parameters(index = "0", description = "Task title")
    private String title;
    
    @Option(names = {"-d", "--description"}, description = "Task description")
    private String description;
    
    @Option(names = {"-p", "--priority"}, description = "Task priority: LOW, MEDIUM, HIGH")
    private Task.Priority priority = Task.Priority.MEDIUM;
    
    @Override
    public Integer call() {
        try {
            TaskService service = TodoApp.getTaskService();
            Task task = service.createTask(title, description, priority);
            System.out.println("✅ Task added successfully!");
            System.out.println("ID: " + task.getId() + " | Title: " + task.getTitle());
            return 0;
        } catch (Exception e) {
            System.err.println("❌ Error adding task: " + e.getMessage());
            return 1;
        }
    }
}