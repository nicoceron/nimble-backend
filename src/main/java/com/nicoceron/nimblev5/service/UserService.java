package com.nicoceron.nimblev5.service;

import com.nicoceron.nimblev5.dao.UserDao;
import com.nicoceron.nimblev5.domain.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.Optional;

// Use @ApplicationScoped and manual transaction handling (@Transactional from JTA or DeltaSpike)
// if you don't want to use EJB. @Stateless is often simpler.
@Stateless
public class UserService {

    @Inject
    private UserDao userDao;

    // Inject a password hashing service here (e.g., BCrypt)
    // For example: @Inject private PasswordHashingService hashingService;

    public User registerUser(String username, String email, String plainPassword) {
        // 1. Check if username or email already exists
        if (userDao.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        if (userDao.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }

        // 2. Hash the password (!! IMPORTANT !!)
        // Replace with actual hashing call
        // String hashedPassword = hashingService.hashPassword(plainPassword);
        String hashedPassword = hashPasswordPlaceholder(plainPassword); // Replace this placeholder!

        // 3. Create and persist user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPasswordHash(hashedPassword); // Store the HASH

        userDao.persist(newUser); // Transaction managed by @Stateless
        return newUser;
    }

    public Optional<User> loginUser(String username, String plainPassword) {
        System.out.println("Attempting login for username: " + username); // Log input
        Optional<User> userOpt = userDao.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String storedHash = user.getPasswordHash(); // Get the stored hash
            System.out.println("Stored hash for user: " + storedHash); // Log hash
            System.out.println("Password provided for login: " + plainPassword); // Log plain password

            // --- Your Password Check Logic ---
            boolean passwordMatches = checkPasswordPlaceholder(plainPassword, storedHash); // Or your real check method
            // --------------------------------

            System.out.println("Password check result: " + passwordMatches); // Log the result

            if (passwordMatches) {
                System.out.println("Login successful for: " + username);
                return Optional.of(user);
            } else {
                System.out.println("Password mismatch for: " + username);
            }
        } else {
            System.out.println("User not found: " + username);
        }
        return Optional.empty(); // Login failed
    }

    public Optional<User> findUserById(Long userId) {
        return userDao.findById(userId);
    }

    // --- !! Placeholder Hashing Methods - REPLACE THESE !! ---
    private String hashPasswordPlaceholder(String plainPassword) {
        // In a real app, use a strong hashing algorithm like BCrypt
        // Example using a simple (INSECURE) method for demonstration only:
        System.err.println("WARNING: Using insecure placeholder password hashing!");
        return "hashed_" + new StringBuilder(plainPassword).reverse().toString();
    }

    private boolean checkPasswordPlaceholder(String plainPassword, String storedHash) {
        System.err.println("WARNING: Using insecure placeholder password checking!");
        return storedHash.equals(hashPasswordPlaceholder(plainPassword));
    }
    // --- End Placeholder ---

    // Add other user-related business logic (update profile, change password etc.)
}