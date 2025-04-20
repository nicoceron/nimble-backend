package com.nicoceron.nimblev5.dao; // Adjust package if needed

import com.nicoceron.nimblev5.domain.Task;
import com.nicoceron.nimblev5.domain.TaskStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnitUtil;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TaskDao {

    // Inject EntityManager directly here
    @PersistenceContext(unitName = "NimblePU")
    protected EntityManager entityManager;

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Task.class, id));
    }

    public List<Task> findAll() {
        return entityManager.createQuery("SELECT e FROM Task e", Task.class)
                .getResultList();
    }

    public void persist(Task entity) {
        entityManager.persist(entity);
    }

    public Task merge(Task entity) {
        return entityManager.merge(entity);
    }

    public void remove(Task entity) {
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
        } else {
            PersistenceUnitUtil unitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
            Long entityId = (Long) unitUtil.getIdentifier(entity);
            Task managedEntity = entityManager.find(Task.class, entityId);
            if (managedEntity != null) {
                entityManager.remove(managedEntity);
            }
        }
    }

    public void removeById(Long id) {
        findById(id).ifPresent(this::remove);
    }

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