package com.turf.service;

import com.turf.dao.UserDAO;
import com.turf.model.User;

/**
 * Service class for user authentication operations
 */
public class AuthService {
    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Register a new user
     * @param name User name
     * @param email User email
     * @param password User password
     * @param role User role (ADMIN or CUSTOMER)
     * @return true if registration successful, false otherwise
     */
    public boolean registerUser(String name, String email, String password, String role) {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty!");
            return false;
        }
        
        if (email == null || email.trim().isEmpty() || !isValidEmail(email)) {
            System.out.println("Please provide a valid email address!");
            return false;
        }
        
        if (password == null || password.trim().isEmpty() || password.length() < 6) {
            System.out.println("Password must be at least 6 characters long!");
            return false;
        }
        
        if (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("CUSTOMER")) {
            System.out.println("Role must be either ADMIN or CUSTOMER!");
            return false;
        }

        // Check if email already exists
        if (userDAO.emailExists(email)) {
            System.out.println("Email already exists! Please use a different email.");
            return false;
        }

        // Create user object and register
        User user = new User(name.trim(), email.trim().toLowerCase(), password, role.toUpperCase());
        boolean success = userDAO.registerUser(user);
        
        if (success) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Registration failed! Please try again.");
        }
        
        return success;
    }

    /**
     * Login user with email and password
     * @param email User email
     * @param password User password
     * @return User object if login successful, null otherwise
     */
    public User loginUser(String email, String password) {
        // Validate input
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email cannot be empty!");
            return null;
        }
        
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Password cannot be empty!");
            return null;
        }

        // Attempt login
        User user = userDAO.loginUser(email.trim().toLowerCase(), password);
        
        if (user != null) {
            System.out.println("Login successful! Welcome, " + user.getName() + "!");
        } else {
            System.out.println("Invalid email or password!");
        }
        
        return user;
    }

    /**
     * Validate email format
     * @param email Email to validate
     * @return true if email is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    /**
     * Get user by ID
     * @param userId User ID
     * @return User object if found, null otherwise
     */
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    /**
     * Check if user has admin role
     * @param user User object
     * @return true if user is admin, false otherwise
     */
    public boolean isAdmin(User user) {
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    /**
     * Check if user has customer role
     * @param user User object
     * @return true if user is customer, false otherwise
     */
    public boolean isCustomer(User user) {
        return user != null && "CUSTOMER".equalsIgnoreCase(user.getRole());
    }
}
