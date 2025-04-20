package com.nicoceron.nimblev5.dao; // Adjust package if needed

import com.nicoceron.nimblev5.domain.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnitUtil;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserDao {

    // Inject EntityManager directly here
    @PersistenceContext(unitName = "NimblePU") // Use the correct persistence unit name
    protected EntityManager entityManager;

    // --- Re-implement basic CRUD using the injected EntityManager ---

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    public List<User> findAll() {
        return entityManager.createQuery("SELECT e FROM User e", User.class)
                .getResultList();
    }

    public void persist(User entity) {
        entityManager.persist(entity);
    }

    public User merge(User entity) {
        return entityManager.merge(entity);
    }

    public void remove(User entity) {
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
        } else {
            PersistenceUnitUtil unitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
            Long entityId = (Long) unitUtil.getIdentifier(entity);
            User managedEntity = entityManager.find(User.class, entityId);
            if (managedEntity != null) {
                entityManager.remove(managedEntity);
            }
        }
    }

    public void removeById(Long id) {
        findById(id).ifPresent(this::remove);
    }

    // --- Keep your specific finders ---

    public Optional<User> findByUsername(String username) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}