package com.nicoceron.nimblev5.domain; // Ensure package is correct

import jakarta.persistence.*;
// Add this JAXB import:
import jakarta.xml.bind.annotation.XmlTransient;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "USERS")
@SequenceGenerator(name = "users_seq_gen",
        sequenceName = "USERS_SEQ",
        allocationSize = 1)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_gen")
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "created_date", nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    // This is the relationship causing the cycle when combined with Task.user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;

    // Getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    // *** ADD @XmlTransient HERE ***
    @XmlTransient // Tell JAXB (XML serializer) to ignore this getter
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; User user = (User) o; return Objects.equals(userId, user.userId); }
    @Override
    public int hashCode() { return Objects.hash(userId); }
    @Override
    public String toString() { return "User{" + "userId=" + userId + ", username='" + username + '\'' + ", email='" + email + '\'' + ", createdDate=" + createdDate + '}'; }
}