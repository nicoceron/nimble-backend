package com.nicoceron.nimblev5.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnitUtil;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

/**
 * Abstract base class for Data Access Objects providing basic CRUD operations.
 *
 * @param <T>  The entity type.
 * @param <ID> The primary key type for the entity.
 */
public abstract class AbstractDao<T, ID> {

    /**
     * The EntityManager is injected by the container (GlassFish).
     * It's marked as 'protected' so subclasses (UserDao, TaskDao) can access it directly.
     */
    @PersistenceContext(unitName = "NimblePU") // Specify your persistence unit name from persistence.xml
    protected EntityManager entityManager;

    private final Class<T> entityClass;

    /**
     * Constructor that determines the entity class type using reflection.
     */
    @SuppressWarnings("unchecked")
    public AbstractDao() {
        // This reflection finds the actual type argument (like User or Task)
        // used when a subclass extends AbstractDao<User, Long> or AbstractDao<Task, Long>
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Finds an entity by its primary key.
     *
     * @param id The primary key.
     * @return An Optional containing the entity if found, otherwise empty.
     */
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    /**
     * Finds all entities of this type.
     *
     * @return A list of all entities.
     */
    public List<T> findAll() {
        // Creates a basic JPQL query "SELECT e FROM EntityName e"
        return entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
    }

    /**
     * Persists (saves) a new entity in the database.
     * Requires an active transaction (managed by @Stateless EJB or manually).
     *
     * @param entity The entity to persist.
     */
    public void persist(T entity) {
        entityManager.persist(entity);
    }

    /**
     * Merges the state of the given entity into the current persistence context.
     * Useful for saving updates to detached entities.
     * Requires an active transaction.
     *
     * @param entity The entity to merge.
     * @return The managed instance that the state was merged to.
     */
    public T merge(T entity) {
        return entityManager.merge(entity);
    }

    /**
     * Removes an entity instance.
     * Requires an active transaction.
     * Handles both managed and detached entities.
     *
     * @param entity The entity to remove.
     */
    public void remove(T entity) {
        if (entityManager.contains(entity)) {
            // If entity is managed, remove directly
            entityManager.remove(entity);
        } else {
            // If entity is detached, find its managed version first using its ID
            PersistenceUnitUtil unitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
            @SuppressWarnings("unchecked") // Suppress warning for casting ID type
            ID entityId = (ID) unitUtil.getIdentifier(entity); // Get the ID from the detached entity
            T managedEntity = entityManager.find(entityClass, entityId); // Find the managed version
            if (managedEntity != null) {
                entityManager.remove(managedEntity); // Remove the managed version
            }
        }
    }

    /**
     * Removes an entity by its primary key.
     * Requires an active transaction.
     *
     * @param id The primary key of the entity to remove.
     */
    public void removeById(ID id) {
        findById(id).ifPresent(this::remove); // Find the entity first, then call remove()
    }

    /**
     * Provides direct access to the EntityManager for subclasses
     * if they need more complex query capabilities.
     *
     * @return The injected EntityManager instance.
     */
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}