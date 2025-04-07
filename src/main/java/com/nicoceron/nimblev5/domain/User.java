package com.nicoceron.nimblev5.domain; // Adjust package name if needed

import jakarta.persistence.*;
import java.util.Date; // Changed import
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "USERS") // Match your table name exactly
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Correct for Oracle 12c+ Identity Columns
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash; // Store hashed password, not plain text!

    @Column(name = "created_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP) // Specifies mapping to DB TIMESTAMP
    private Date createdDate; // Changed type to java.util.Date

    // Relationship: One User has Many Tasks
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;

    // --- Getters and Setters (Updated for Date) ---

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        // Remember to hash the password in your service layer before setting it here
        this.passwordHash = passwordHash;
    }

    public Date getCreatedDate() { // Return type changed
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) { // Parameter type changed
        this.createdDate = createdDate;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    // --- equals() and hashCode() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    // --- toString() (Optional) ---

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}