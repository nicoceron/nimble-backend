package com.nicoceron.nimblev5.service;

import com.nicoceron.nimblev5.dao.TaskDao;
import com.nicoceron.nimblev5.dao.UserDao;
import com.nicoceron.nimblev5.domain.Task;
import com.nicoceron.nimblev5.domain.TaskPriority;
import com.nicoceron.nimblev5.domain.TaskStatus;
import com.nicoceron.nimblev5.domain.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Stateless
public class TaskService {

    @Inject
    private TaskDao taskDao;

    @Inject
    private UserDao userDao; // To associate tasks with users

    public Task createTask(Long userId, String title, String description, Timestamp dueDate, TaskPriority priority) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Task newTask = new Task();
        newTask.setUser(user);
        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setDueDate(dueDate);
        newTask.setPriority(priority);
        newTask.setStatus(TaskStatus.PENDING); // Default status

        taskDao.persist(newTask);
        return newTask;
    }

    public Optional<Task> findTaskById(Long taskId) {
        return taskDao.findById(taskId);
    }

    public List<Task> findTasksByUserId(Long userId) {
        // Ensure user exists (optional, depends on requirements)
        // userDao.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return taskDao.findByUserId(userId);
    }

    public Task updateTask(Long taskId, String title, String description, Timestamp dueDate, TaskPriority priority, TaskStatus status) {
        Task existingTask = taskDao.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));

        // Update fields (add null checks if partial updates are allowed)
        existingTask.setTitle(title);
        existingTask.setDescription(description);
        existingTask.setDueDate(dueDate);
        existingTask.setPriority(priority);
        existingTask.setStatus(status);
        // last_modified_date is updated by the DB trigger

        return taskDao.merge(existingTask); // Use merge for updates
    }

    public void deleteTask(Long taskId) {
        // Optional: Check if task exists before trying to delete
        // Task task = taskDao.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
        taskDao.removeById(taskId);
    }

    // Add more specific business logic (e.g., changeTaskStatus)
}