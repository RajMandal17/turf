package com.turf.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database utility class for managing database connections and initialization
 */
public class DatabaseUtil {
    private static final String URL = "jdbc:mariadb://localhost:3306/turf_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Koinpark@123"; // MariaDB password
    
    private static Connection connection = null;

    /**
     * Get database connection
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load MariaDB JDBC driver
                Class.forName("org.mariadb.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MariaDB JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Initialize database and create tables if they don't exist
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create database if it doesn't exist
            String createDatabase = "CREATE DATABASE IF NOT EXISTS turf_management";
            stmt.executeUpdate(createDatabase);
            
            // Use the database
            stmt.executeUpdate("USE turf_management");

            // Create users table
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    password VARCHAR(100) NOT NULL,
                    role ENUM('ADMIN', 'CUSTOMER') NOT NULL
                )
                """;
            stmt.executeUpdate(createUsersTable);

            // Create turfs table
            String createTurfsTable = """
                CREATE TABLE IF NOT EXISTS turfs (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    location VARCHAR(200) NOT NULL,
                    hourly_rate DECIMAL(10,2) NOT NULL
                )
                """;
            stmt.executeUpdate(createTurfsTable);

            // Create bookings table
            String createBookingsTable = """
                CREATE TABLE IF NOT EXISTS bookings (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT NOT NULL,
                    turf_id INT NOT NULL,
                    booking_time DATETIME NOT NULL,
                    duration INT NOT NULL,
                    total_cost DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                    FOREIGN KEY (turf_id) REFERENCES turfs(id) ON DELETE CASCADE
                )
                """;
            stmt.executeUpdate(createBookingsTable);

            // Insert default admin user if not exists
            String insertAdmin = """
                INSERT IGNORE INTO users (name, email, password, role) 
                VALUES ('Admin', 'admin@turf.com', 'admin123', 'ADMIN')
                """;
            stmt.executeUpdate(insertAdmin);

            System.out.println("Database and tables initialized successfully!");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
