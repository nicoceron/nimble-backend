# Nimble Task Management - Java Backend (SOAP API)

This is the Java backend service for the Nimble Task Management system. It provides a SOAP web service API for managing users and tasks, designed to run on a Jakarta EE application server and interact with an Oracle database.

## Architecture Overview

* **Overall System Style:** Service-Oriented Architecture (SOA)
* **Component:** Backend Service
* **Platform:** Jakarta EE
* **Language:** Java
* **Architecture Pattern:** Layered Architecture (Web Service -> Service -> DAO -> Domain/Persistence)
* **Integration Protocol:** SOAP (via JAX-WS)
* **Application Server:** Glassfish (Target)
* **Database:** Oracle (Target)

## Features

* **User Management:**
    * User registration with username, email, and password. (Password hashing included, though currently a placeholder needing replacement).
    * User login validation based on username and password.
    * Checks for existing usernames and emails during registration.
* **Task Management:**
    * Create tasks associated with a user, including title, description, due date (optional), and priority.
    * Retrieve tasks for a specific user.
    * Retrieve a specific task by its ID.
    * Update existing tasks (title, description, due date, priority, status).
    * Delete tasks by ID.
* **API:**
    * Exposes functionality through SOAP web services (`UserService` implied, `TaskService` explicitly defined via `TaskSoapService`).

## Technical Details

* **Framework:** Jakarta EE (using `@Stateless` EJB, `@Inject`, `@WebService`).
* **Persistence:** Jakarta Persistence API (JPA) using `@Entity`, `@PersistenceContext`. Sequence generation configured for Oracle compatibility (`@SequenceGenerator`).
* **Database Interaction:** Data Access Objects (DAOs) (`UserDao`, `TaskDao`) handle database operations using `EntityManager`.
* **Web Service:** JAX-WS for SOAP API endpoints (`TaskSoapService`).
* **Structure (Layered):**
    * `ws`: Web Service Endpoints (`TaskSoapService`).
    * `service`: Business Logic (`UserService`, `TaskService`).
    * `dao`: Data Access Objects (`UserDao`, `TaskDao`).
    * `domain`: JPA Entities (`User`, `Task`) and Enums (`TaskStatus`, `TaskPriority`).

## Setup

1.  **Database:** Set up an Oracle database. Ensure the sequences specified in the `@SequenceGenerator` annotations (`USERS_SEQ`, `TASK_SEQ`) exist or are created. Configure the persistence unit (`NimblePU` referenced in DAOs) in `persistence.xml` (not provided) with the correct Oracle database connection details (driver, URL, user, password).
2.  **Application Server:** Deploy the application (likely as a WAR or EAR file) to a Glassfish server.
3.  **Password Hashing:** **CRITICAL: Replace the placeholder password hashing and checking logic in `UserService.java` with a secure implementation (e.g., BCrypt) before any production use.**
4.  **Dependencies:** Ensure all necessary Jakarta EE APIs and implementation dependencies (JPA provider like Hibernate/EclipseLink, JAX-WS implementation, Oracle JDBC driver) are available on the Glassfish server or included in the deployment.

## Key Files

* `ws/TaskSoapService.java`: Defines the SOAP API endpoints for task operations.
* `service/UserService.java`: Contains business logic for user registration and login.
* `service/TaskService.java`: Contains business logic for CRUD operations on tasks.
* `dao/UserDao.java`: Handles database access for User entities.
* `dao/TaskDao.java`: Handles database access for Task entities.
* `domain/User.java`: JPA entity for users.
* `domain/Task.java`: JPA entity for tasks.
* `domain/TaskStatus.java`, `domain/TaskPriority.java`: Enums for task status and priority.

## Notes

* The `User` entity uses `@XmlTransient` on the `getTasks()` method to prevent potential serialization issues (circular references) when generating SOAP responses.
* The `TaskSoapService` and `TaskService` handle `java.util.Date` for `dueDate`, converting to `java.sql.Timestamp` where necessary for the persistence layer.
