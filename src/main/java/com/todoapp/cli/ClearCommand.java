package com.todoapp.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import com.todoapp.repository.TaskRepository;
import com.todoapp.service.TaskService;
import java.util.concurrent.Callable;

@Command(name = "clear", description = "Clear all tasks", mixinStandardHelpOptions = true)
class ClearCommand implements Callable<Integer> {
    
    @Option(names = {"-f", "--force"}, description = "Force clear without confirmation")
    private boolean force;
    
    @Override
    public Integer call() {
        try {
            TaskService service = TodoApp.getTaskService();
            long taskCount = service.getTaskCount();
            
            if (taskCount == 0) {
                System.out.println("📝 No tasks to clear.");
                return 0;
            }
            
            if (!force) {
                System.out.println("⚠️  This will delete ALL " + taskCount + " tasks!");
                System.out.print("Are you sure? (yes/no): ");
                
                java.util.Scanner scanner = new java.util.Scanner(System.in);
                String confirmation = scanner.nextLine().trim().toLowerCase();
                
                if (!confirmation.equals("yes") && !confirmation.equals("y")) {
                    System.out.println("❌ Clear cancelled.");
                    return 0;
                }
            }
            
            // Получаем доступ к репозиторию и очищаем
            java.lang.reflect.Field repoField = TaskService.class.getDeclaredField("repository");
            repoField.setAccessible(true);
            TaskRepository repository = (TaskRepository) repoField.get(service);
            repository.clear();
            
            System.out.println("🗑️  Cleared all " + taskCount + " tasks.");
            return 0;
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            return 1;
        }
    }
}