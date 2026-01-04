package com.todoapp.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import com.todoapp.model.Task;
import com.todoapp.service.TaskService;
import java.util.concurrent.Callable;

@Command(name = "init", description = "Initialize with sample tasks", mixinStandardHelpOptions = true)
class InitCommand implements Callable<Integer> {
    
    @Override
    public Integer call() {
        try {
            TaskService service = TodoApp.getTaskService();
            
            // Проверяем есть ли уже задачи
            if (service.getTaskCount() > 0) {
                System.out.println("⚠️  Tasks already exist. Use --force to overwrite.");
                return 0;
            }
            
            // Добавляем примеры
            service.createTask("Complete project documentation", 
                             "Write README and architecture docs", 
                             Task.Priority.HIGH);
            service.createTask("Setup CI/CD pipeline", 
                             "Configure GitHub Actions", 
                             Task.Priority.MEDIUM);
            service.createTask("Write unit tests", 
                             "Cover all service methods", 
                             Task.Priority.MEDIUM);
            
            System.out.println("✅ 3 sample tasks added successfully!");
            System.out.println("Use 'todo list' to see them.");
            return 0;
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            return 1;
        }
    }
}