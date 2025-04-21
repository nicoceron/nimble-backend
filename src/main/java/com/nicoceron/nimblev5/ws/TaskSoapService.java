package com.nicoceron.nimblev5.ws;

import com.nicoceron.nimblev5.domain.Task;
import com.nicoceron.nimblev5.domain.TaskPriority;
import com.nicoceron.nimblev5.domain.TaskStatus;
import com.nicoceron.nimblev5.service.TaskService;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.sql.Timestamp; // Keep this import if original method uses it
import java.util.List;

@WebService(serviceName = "TaskService")
public class TaskSoapService {

    @Inject
    private TaskService taskService;

    // --- Original method that requires dueDate ---
    @WebMethod
    public Task createTask(@WebParam(name = "userId") Long userId,
                           @WebParam(name = "title") String title,
                           @WebParam(name = "description") String description,
                           @WebParam(name = "dueDate") Timestamp dueDate, // Required here
                           @WebParam(name = "priority") TaskPriority priority) {
        if (userId == null || title == null || title.isEmpty()) {
            throw new IllegalArgumentException("User ID and Title are required.");
        }
        if (dueDate == null) {
            // Depending on your backend type mapping, null might cause issues here still
            // If you only ever call the WithoutDate version, this method might not be used by Android
            // Or consider changing this parameter to java.util.Date if Timestamp causes issues
            throw new IllegalArgumentException("Due Date is required for this operation version.");
        }
        return taskService.createTask(userId, title, description, dueDate, priority);
    }

    // --- ADDED Overloaded Method without dueDate ---
    @WebMethod(operationName = "createTaskWithoutDate") // Explicitly name operation
    public Task createTaskWithoutDate(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "title") String title,
            @WebParam(name = "description") String description,
            // No dueDate parameter here
            @WebParam(name = "priority") TaskPriority priority) {
        // Basic validation
        if (userId == null || title == null || title.isEmpty()) {
            throw new IllegalArgumentException("User ID and Title are required.");
        }
        // Call the underlying service, passing null for the dueDate
        // This assumes taskService handles null dueDate gracefully (which it should if DB allows NULL)
        return taskService.createTask(userId, title, description, null, priority);
    }
    // --- End of New Method ---

    @WebMethod
    public Task getTaskById(@WebParam(name = "taskId") Long taskId) {
        return taskService.findTaskById(taskId)
                .orElse(null);
    }

    @WebMethod
    public List<Task> getTasksForUser(@WebParam(name = "userId") Long userId) {
        return taskService.findTasksByUserId(userId);
    }

    @WebMethod
    public Task updateTask(@WebParam(name = "taskId") Long taskId,
                           @WebParam(name = "title") String title,
                           @WebParam(name = "description") String description,
                           @WebParam(name = "dueDate") Timestamp dueDate,
                           @WebParam(name = "priority") TaskPriority priority,
                           @WebParam(name = "status") TaskStatus status) {
        if (taskId == null) throw new IllegalArgumentException("Task ID is required for update.");
        return taskService.updateTask(taskId, title, description, dueDate, priority, status);
    }

    @WebMethod
    public boolean deleteTask(@WebParam(name = "taskId") Long taskId) {
        try {
            taskService.deleteTask(taskId);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting task ID " + taskId + ": " + e.getMessage()); // Log error
            // Consider throwing a specific SOAP Fault Exception
            return false;
        }
    }
}