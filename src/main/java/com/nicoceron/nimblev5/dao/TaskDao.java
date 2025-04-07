package com.nicoceron.nimblev5.dao;

import com.nicoceron.nimblev5.domain.Task;
import com.nicoceron.nimblev5.domain.TaskStatus;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TaskDao extends AbstractDao<Task, Long> {

    public List<Task> findByUserId(Long userId) {
        return entityManager.createQuery("SELECT t FROM Task t WHERE t.user.userId = :userId ORDER BY t.dueDate ASC, t.priority ASC", Task.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<Task> findByUserIdAndStatus(Long userId, TaskStatus status) {
        return entityManager.createQuery("SELECT t FROM Task t WHERE t.user.userId = :userId AND t.status = :status ORDER BY t.dueDate ASC, t.priority ASC", Task.class)
                .setParameter("userId", userId)
                .setParameter("status", status)
                .getResultList();
    }

    // Add other specific finders as needed (e.g., findByPriority, findByDueDateRange)
}