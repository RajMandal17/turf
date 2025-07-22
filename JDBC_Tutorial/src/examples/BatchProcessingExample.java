package examples;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * This example demonstrates batch processing in JDBC
 * It adds multiple new employees to the database in a single batch
 */
public class BatchProcessingExample {
    
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            // Establish connection
            connection = DatabaseUtil.getConnection();
            
            // Create the SQL statement with parameters
            String sql = "INSERT INTO employees (first_name, last_name, email, department, salary, hire_date) "
                       + "VALUES (?, ?, ?, ?, ?, ?)";
            
            // Create PreparedStatement
            pstmt = connection.prepareStatement(sql);
            
            // Prepare batch of employee records
            System.out.println("Preparing batch insert of new employees...");
            
            // Employee 1
            addEmployeeToBatch(pstmt, "Thomas", "Anderson", "thomas.anderson@example.com", 
                    "Engineering", 83000.00, LocalDate.of(2022, 2, 15));
            
            // Employee 2
            addEmployeeToBatch(pstmt, "Alice", "Johnson", "alice.johnson@example.com",
                    "Marketing", 71000.00, LocalDate.of(2022, 3, 10));
            
            // Employee 3
            addEmployeeToBatch(pstmt, "Mark", "Wilson", "mark.wilson@example.com",
                    "Sales", 74000.00, LocalDate.of(2022, 1, 20));
            
            // Employee 4
            addEmployeeToBatch(pstmt, "Sophia", "Garcia", "sophia.garcia@example.com",
                    "Human Resources", 69000.00, LocalDate.of(2022, 4, 5));
            
            // Employee 5
            addEmployeeToBatch(pstmt, "William", "Brown", "william.brown@example.com",
                    "Engineering", 86000.00, LocalDate.of(2022, 2, 28));
            
            System.out.println("Executing batch...");
            
            // Execute the batch
            int[] updateCounts = pstmt.executeBatch();
            
            // Display results
            System.out.println("Batch execution completed!");
            System.out.println("Results:");
            
            int totalInserted = 0;
            for (int i = 0; i < updateCounts.length; i++) {
                if (updateCounts[i] >= 0) {
                    System.out.println("Employee " + (i + 1) + ": Successfully added");
                    totalInserted++;
                } else if (updateCounts[i] == PreparedStatement.SUCCESS_NO_INFO) {
                    System.out.println("Employee " + (i + 1) + ": Successfully added (no info)");
                    totalInserted++;
                } else if (updateCounts[i] == PreparedStatement.EXECUTE_FAILED) {
                    System.out.println("Employee " + (i + 1) + ": Insert failed");
                }
            }
            
            System.out.println("Total employees inserted: " + totalInserted);
            
        } catch (SQLException e) {
            System.err.println("Database error occurred!");
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (pstmt != null) pstmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void addEmployeeToBatch(PreparedStatement pstmt, String firstName, 
            String lastName, String email, String department, double salary, LocalDate hireDate) 
            throws SQLException {
        
        pstmt.setString(1, firstName);
        pstmt.setString(2, lastName);
        pstmt.setString(3, email);
        pstmt.setString(4, department);
        pstmt.setDouble(5, salary);
        pstmt.setDate(6, Date.valueOf(hireDate));
        
        // Add to batch
        pstmt.addBatch();
    }
}
