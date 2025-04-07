package com.nicoceron.nimblev5.domain; // Adjust package name if needed

import jakarta.persistence.*;
import java.util.Date; // Changed import
import java.util.Objects;

@Entity
@Table(name = "TASK") // Match your table name
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    // Many Tasks belong to One User
    @ManyToOne(fetch = FetchType.LAZY) // Eager fetching can cause performance issues
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "due_date")
    @Temporal(TemporalType.TIMESTAMP) // Specifies mapping to DB TIMESTAMP
    private Date dueDate; // Changed type to java.util.Date

    @Enumerated(EnumType.STRING) // Store enum name ('HIGH', 'MEDIUM', 'LOW')
    @Column(name = "priority", length = 10)
    private TaskPriority priority; // Assumes TaskPriority enum exists

    @Enumerated(EnumType.STRING) // Store enum name ('PENDING', etc.)
    @Column(name = "status", length = 15)
    private TaskStatus status; // Assumes TaskStatus enum exists

    @Column(name = "created_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP) // Specifies mapping to DB TIMESTAMP
    private Date createdDate; // Changed type to java.util.Date

    // Let the database trigger handle this
    @Column(name = "last_modified_date", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP) // Specifies mapping to DB TIMESTAMP
    private Date lastModifiedDate; // Changed type to java.util.Date

    // --- Getters and Setters (Updated for Date) ---

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() { // Return type changed
        return dueDate;
    }

    public void setDueDate(Date dueDate) { // Parameter type changed
        this.dueDate = dueDate;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Date getCreatedDate() { // Return type changed
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) { // Parameter type changed
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() { // Return type changed
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) { // Parameter type changed
        this.lastModifiedDate = lastModifiedDate;
    }

    // --- equals() and hashCode() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }

    // --- toString() (Optional) ---

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", userId=" + (user != null ? user.getUserId() : null) + // Avoid NPE if user is null
                ", title='" + title + '\'' +
                ", dueDate=" + dueDate +
                ", priority=" + priority +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}