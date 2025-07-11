-- Turf Management System Database Setup
-- Run this script in MySQL to set up the database manually if needed

CREATE DATABASE IF NOT EXISTS turf_management;
USE turf_management;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'CUSTOMER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Turfs table
CREATE TABLE IF NOT EXISTS turfs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(200) NOT NULL,
    hourly_rate DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    turf_id INT NOT NULL,
    booking_time DATETIME NOT NULL,
    duration INT NOT NULL,
    total_cost DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (turf_id) REFERENCES turfs(id) ON DELETE CASCADE
);

-- Insert default admin user
INSERT IGNORE INTO users (name, email, password, role) 
VALUES ('Admin', 'admin@turf.com', 'admin123', 'ADMIN');

-- Insert sample turfs
INSERT IGNORE INTO turfs (name, location, hourly_rate) VALUES
('Green Field Arena', '123 Main Street, Downtown', 25.00),
('Blue Sky Sports Complex', '456 Oak Avenue, Midtown', 30.00),
('Red Rock Football Ground', '789 Pine Road, Uptown', 35.00);

-- Insert sample customer
INSERT IGNORE INTO users (name, email, password, role) 
VALUES ('John Doe', 'john@customer.com', 'customer123', 'CUSTOMER');

-- Show created tables
SHOW TABLES;

-- Display sample data
SELECT 'Users' as 'Table Name';
SELECT * FROM users;

SELECT 'Turfs' as 'Table Name';
SELECT * FROM turfs;

SELECT 'Bookings' as 'Table Name';
SELECT * FROM bookings;
