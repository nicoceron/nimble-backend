package com.nicoceron.nimblev5.ws;

import com.nicoceron.nimblev5.domain.Task;
import com.nicoceron.nimblev5.domain.TaskPriority;
import com.nicoceron.nimblev5.domain.TaskStatus;
import com.nicoceron.nimblev5.service.TaskService;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

// Import java.util.Date instead of java.sql.Timestamp for parameters
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@WebService(serviceName = "TaskService")
public class TaskSoapService {

    @Inject
    private TaskService taskService; // NOTE: TaskService must also be updated

    // --- Method modified to use java.util.Date ---
    @WebMethod
    public Task createTask(@WebParam(name = "userId") Long userId,
                           @WebParam(name = "title") String title,
                           @WebParam(name = "description") String description,
                           @WebParam(name = "dueDate") Date dueDate, // Changed type to Date
                           @WebParam(name = "priority") TaskPriority priority) {
        if (userId == null || title == null || title.isEmpty()) {
            throw new IllegalArgumentException("User ID and Title are required.");
        }
        // Removed the explicit null check for dueDate here,
        // let the underlying service handle null if appropriate.
        // Passing null Date should be fine if the DB column allows nulls.

        // IMPORTANT: Assumes taskService.createTask is updated to accept java.util.Date
        return taskService.createTask(userId, title, description, (Timestamp) dueDate, priority);
    }

    // --- Method without dueDate (no changes needed here) ---
    @WebMethod(operationName = "createTaskWithoutDate")
    public Task createTaskWithoutDate(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "title") String title,
            @WebParam(name = "description") String description,
            @WebParam(name = "priority") TaskPriority priority) {
        if (userId == null || title == null || title.isEmpty()) {
            throw new IllegalArgumentException("User ID and Title are required.");
        }
        // IMPORTANT: Assumes taskService.createTask is updated to accept java.util.Date
        // We are passing null here, which should now be a null java.util.Date
        return taskService.createTask(userId, title, description, null, priority);
    }

    @WebMethod
    public Task getTaskById(@WebParam(name = "taskId") Long taskId) {
        return taskService.findTaskById(taskId)
                .orElse(null);
    }

    @WebMethod
    public List<Task> getTasksForUser(@WebParam(name = "userId") Long userId) {
        return taskService.findTasksByUserId(userId);
    }

    // --- Method modified to use java.util.Date ---
    @WebMethod
    public Task updateTask(@WebParam(name = "taskId") Long taskId,
                           @WebParam(name = "title") String title,
                           @WebParam(name = "description") String description,
                           @WebParam(name = "dueDate") Date dueDate, // Changed type to Date
                           @WebParam(name = "priority") TaskPriority priority,
                           @WebParam(name = "status") TaskStatus status) {
        if (taskId == null) throw new IllegalArgumentException("Task ID is required for update.");

        // IMPORTANT: Assumes taskService.updateTask is updated to accept java.util.Date
        return taskService.updateTask(taskId, title, description, (Timestamp) dueDate, priority, status);
    }

    @WebMethod
    public boolean deleteTask(@WebParam(name = "taskId") Long taskId) {
        try {
            taskService.deleteTask(taskId);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting task ID " + taskId + ": " + e.getMessage()); // Log error
            return false;
        }
    }
}