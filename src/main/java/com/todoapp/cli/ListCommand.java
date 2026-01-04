package com.todoapp.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import com.todoapp.model.Task;
import com.todoapp.service.TaskService;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "list", description = "List all tasks", mixinStandardHelpOptions = true)
class ListCommand implements Callable<Integer> {
    
    @Option(names = {"-a", "--all"}, description = "Show all tasks (default)")
    private boolean showAll = true;
    
    @Option(names = {"-c", "--completed"}, description = "Show only completed tasks")
    private boolean showCompleted;
    
    @Option(names = {"-t", "--todo"}, description = "Show only active tasks")
    private boolean showTodo;
    
    @Override
    public Integer call() {
        try {
            TaskService service = TodoApp.getTaskService();
            List<Task> tasks;
            
            if (showCompleted) {
                tasks = service.getCompletedTasks();
                System.out.println("\n📋 COMPLETED TASKS:");
            } else if (showTodo) {
                tasks = service.getActiveTasks();
                System.out.println("\n📋 ACTIVE TASKS:");
            } else {
                tasks = service.getAllTasks();
                System.out.println("\n📋 ALL TASKS:");
            }
            
            if (tasks.isEmpty()) {
                System.out.println("No tasks found.");
            } else {
                for (Task task : tasks) {
                    System.out.println(task.toString());
                    if (task.getDescription() != null && !task.getDescription().isEmpty()) {
                        System.out.println("   📝 " + task.getDescription());
                    }
                    System.out.println("   🕐 Created: " + task.getCreatedAt());
                    if (task.isCompleted()) {
                        System.out.println("   ✅ Completed: " + task.getCompletedAt());
                    }
                    System.out.println();
                }
                
                System.out.println("Total: " + tasks.size() + " task(s)");
            }
            return 0;
        } catch (Exception e) {
            System.err.println("❌ Error listing tasks: " + e.getMessage());
            return 1;
        }
    }
}