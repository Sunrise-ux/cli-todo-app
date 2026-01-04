package com.todoapp.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import com.todoapp.service.TaskService;
import com.todoapp.exception.TaskNotFoundException;
import java.util.concurrent.Callable;

@Command(name = "delete", description = "Delete a task", mixinStandardHelpOptions = true)
class DeleteCommand implements Callable<Integer> {
    
    @Parameters(index = "0", description = "Task ID")
    private Integer taskId;
    
    @Override
    public Integer call() {
        try {
            TaskService service = TodoApp.getTaskService();
            service.deleteTask(taskId);
            System.out.println("🗑️ Task " + taskId + " deleted successfully!");
            return 0;
        } catch (TaskNotFoundException e) {
            System.err.println("❌ Error: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            return 1;
        }
    }
}