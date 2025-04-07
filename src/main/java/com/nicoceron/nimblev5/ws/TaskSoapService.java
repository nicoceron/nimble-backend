package com.nicoceron.nimblev5.ws;

import com.nicoceron.nimblev5.domain.Task;
import com.nicoceron.nimblev5.domain.TaskPriority;
import com.nicoceron.nimblev5.domain.TaskStatus;
import com.nicoceron.nimblev5.service.TaskService;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.sql.Timestamp;
import java.util.List;

@WebService(serviceName = "TaskService") // How the service appears in WSDL
public class TaskSoapService {

    @Inject
    private TaskService taskService; // Inject the business service

    @WebMethod
    public Task createTask(@WebParam(name = "userId") Long userId,
                           @WebParam(name = "title") String title,
                           @WebParam(name = "description") String description,
                           @WebParam(name = "dueDate") Timestamp dueDate, // Consider using XMLGregorianCalendar for better SOAP compatibility
                           @WebParam(name = "priority") TaskPriority priority) {
        // Basic validation could happen here or rely on service layer
        if (userId == null || title == null || title.isEmpty()) {
            throw new IllegalArgumentException("User ID and Title are required.");
        }
        return taskService.createTask(userId, title, description, dueDate, priority);
    }

    @WebMethod
    public Task getTaskById(@WebParam(name = "taskId") Long taskId) {
        return taskService.findTaskById(taskId)
                .orElse(null); // Or throw a specific SOAP Fault
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
        // Add more validation as needed
        return taskService.updateTask(taskId, title, description, dueDate, priority, status);
    }

    @WebMethod
    public boolean deleteTask(@WebParam(name = "taskId") Long taskId) {
        try {
            taskService.deleteTask(taskId);
            return true;
        } catch (Exception e) {
            // Log the exception
            // Consider throwing a specific SOAP Fault
            return false;
        }
    }
}