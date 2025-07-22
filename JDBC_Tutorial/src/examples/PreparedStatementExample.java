package examples;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * This example demonstrates the use of PreparedStatement to prevent SQL injection
 */
public class PreparedStatementExample {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("JDBC PreparedStatement Example");
        System.out.println("-----------------------------");
        
        // Get user input
        System.out.print("Enter department to search: ");
        String departmentSearch = scanner.nextLine();
        
        System.out.print("Enter minimum salary: ");
        double minSalary = scanner.nextDouble();
        
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        
        try {
            // Establish connection
            connection = DatabaseUtil.getConnection();
            
            // Create SQL with placeholders (?)
            String sql = "SELECT * FROM employees WHERE department = ? AND salary >= ?";
            
            // Create PreparedStatement
            pstmt = connection.prepareStatement(sql);
            
            // Set parameters
            pstmt.setString(1, departmentSearch);  // First parameter (department)
            pstmt.setDouble(2, minSalary);         // Second parameter (minSalary)
            
            // Execute query
            resultSet = pstmt.executeQuery();
            
            // Display results
            System.out.println("\nEmployees in " + departmentSearch + " department with salary >= $" + minSalary);
            System.out.println("------------------------------------------------------");
            
            boolean found = false;
            while (resultSet.next()) {
                found = true;
                int id = resultSet.getInt("employee_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String department = resultSet.getString("department");
                double salary = resultSet.getDouble("salary");
                
                System.out.printf("ID: %d, Name: %s %s, Department: %s, Salary: $%.2f%n",
                        id, firstName, lastName, department, salary);
            }
            
            if (!found) {
                System.out.println("No matching employees found.");
            }
            
        } catch (SQLException e) {
            System.err.println("Database error occurred!");
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (pstmt != null) pstmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            scanner.close();
        }
    }
}
