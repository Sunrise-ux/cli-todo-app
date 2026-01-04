package com.todoapp.cli;

import com.todoapp.model.Task;import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;
import com.todoapp.service.TaskService;
import com.todoapp.repository.TaskRepository;
import java.util.concurrent.Callable;

@Command(name = "todo",
        description = "CLI Todo Application - Manage your tasks efficiently",
        mixinStandardHelpOptions = true,
        version = "1.0",
        subcommands = {
            InitCommand.class, ClearCommand.class,
            AddCommand.class,
            ListCommand.class,
            CompleteCommand.class, DeleteCommand.class,
            CommandLine.HelpCommand.class
        })
public class TodoApp implements Callable<Integer> {
    
    @Spec
    CommandSpec spec;
    
    private static TaskService taskService;
    
    public static void main(String[] args) {
        // Initialize services
        TaskRepository repository = new TaskRepository();
        taskService = new TaskService(repository);
        
        
        // Run CLI
        int exitCode = new CommandLine(new TodoApp()).execute(args);
        System.exit(exitCode);
    }
    
    public static TaskService getTaskService() {
        return taskService;
    }
    
    private static void initializeSampleTasks() {
        taskService.createTask("Complete project documentation", 
                              "Write README and architecture docs", 
                              Task.Priority.HIGH);
        taskService.createTask("Setup CI/CD pipeline", 
                              "Configure GitHub Actions", 
                              Task.Priority.MEDIUM);
        taskService.createTask("Write unit tests", 
                              "Cover all service methods", 
                              Task.Priority.MEDIUM);
    }
    
    @Override
    public Integer call() {
        // If no subcommand is provided, show help
        spec.commandLine().usage(System.out);
        return 0;
    }
}