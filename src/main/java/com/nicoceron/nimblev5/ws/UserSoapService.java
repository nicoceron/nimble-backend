package com.nicoceron.nimblev5.ws;

import com.nicoceron.nimblev5.domain.User;
import com.nicoceron.nimblev5.service.UserService;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

import java.util.Optional;

/**
 * SOAP Web Service endpoint for User operations.
 * WARNING: This implementation returns the User entity directly, which exposes
 * the structure of internal fields (like passwordHash) in the WSDL.
 * This is generally discouraged for security reasons. Use DTOs for better practice.
 */
@WebService(serviceName = "UserService", // The name exposed in the WSDL
        targetNamespace = "http://ws.nimblev5.nicoceron.com/") // Define a namespace
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED) // Standard style
public class UserSoapService {

    @Inject
    private UserService userService; // Inject the business logic service

    /**
     * Registers a new user.
     * Remember to implement proper password hashing in UserService!
     * WARNING: Returns the User entity. The passwordHash VALUE is nulled out before sending,
     * but the field itself is still exposed in the service contract (WSDL).
     *
     * @param username      The desired username.
     * @param email         The user's email address.
     * @param plainPassword The user's chosen password (plain text).
     * @return The created User entity with passwordHash set to null, or null if registration fails.
     */
    @WebMethod // Marks this method as a SOAP operation
    public User registerUser(@WebParam(name = "username") String username,
                             @WebParam(name = "email") String email,
                             @WebParam(name = "plainPassword") String plainPassword) {
        try {
            // Basic input validation
            if (username == null || username.trim().isEmpty() ||
                    email == null || email.trim().isEmpty() ||
                    plainPassword == null || plainPassword.isEmpty()) {
                throw new IllegalArgumentException("Username, email, and password cannot be empty.");
            }

            User newUser = userService.registerUser(username, email, plainPassword);

            // *** CRITICAL: Null out sensitive fields before returning ***
            if (newUser != null) {
                newUser.setPasswordHash(null); // Avoid sending the hash back
                // If User entity has other sensitive or unnecessary fields for this context, null them too.
                // Also consider lazy-loaded collections - ensure they are handled (e.g., set tasks to null if not needed)
                // newUser.setTasks(null); // Example if tasks shouldn't be returned here
            }
            return newUser;

        } catch (IllegalArgumentException e) {
            System.err.println("Registration failed: " + e.getMessage());
            // Consider throwing a SOAP Fault Exception
            return null; // Indicate failure
        } catch (Exception e) {
            System.err.println("Unexpected error during registration: " + e.getMessage());
            // Consider throwing a SOAP Fault Exception
            return null; // Indicate failure
        }
    }

    /**
     * Attempts to log in a user.
     * WARNING: Returns the User entity upon success. The passwordHash VALUE is nulled out,
     * but the field itself is still exposed in the service contract (WSDL).
     *
     * @param username      The username to log in with.
     * @param plainPassword The password provided by the user.
     * @return User entity with passwordHash set to null if login is successful, null otherwise.
     */
    @WebMethod
    public User loginUser(@WebParam(name = "username") String username,
                          @WebParam(name = "plainPassword") String plainPassword) {

        Optional<User> userOptional = userService.loginUser(username, plainPassword);

        if (userOptional.isPresent()) {
            User loggedInUser = userOptional.get();
            // *** CRITICAL: Null out sensitive fields before returning ***
            loggedInUser.setPasswordHash(null);
            // loggedInUser.setTasks(null); // Example if tasks shouldn't be returned on login
            return loggedInUser;
        } else {
            return null; // Login failed
        }
    }

    /**
     * Retrieves user information by their ID.
     * WARNING: Returns the User entity. The passwordHash VALUE is nulled out,
     * but the field itself is still exposed in the service contract (WSDL).
     *
     * @param userId The ID of the user to retrieve.
     * @return User entity with passwordHash set to null, or null if not found.
     */
    @WebMethod
    public User getUserById(@WebParam(name = "userId") Long userId) {
        Optional<User> userOptional = userService.findUserById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // *** CRITICAL: Null out sensitive fields before returning ***
            user.setPasswordHash(null);
            // user.setTasks(null); // Example if tasks shouldn't be returned here
            return user;
        } else {
            return null; // Not found
        }
    }

    // Add other user-related web methods as needed
}