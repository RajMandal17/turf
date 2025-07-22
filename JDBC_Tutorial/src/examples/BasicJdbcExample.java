package examples;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Basic JDBC example demonstrating simple database connection and queries
 */
public class BasicJdbcExample {
    
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            // Step 1: Establish connection to database
            System.out.println("Connecting to database...");
            connection = DatabaseUtil.getConnection();
            System.out.println("Connection established successfully!");
            
            // Step 2: Create a statement
            statement = connection.createStatement();
            
            // Step 3: Execute a query
            System.out.println("\nExecuting query: SELECT * FROM employees");
            resultSet = statement.executeQuery("SELECT * FROM employees");
            
            // Step 4: Process the results
            System.out.println("\nEmployee List:");
            System.out.println("------------------------------------------------------");
            System.out.printf("%-3s %-10s %-10s %-25s %-15s %s%n", 
                    "ID", "First Name", "Last Name", "Email", "Department", "Salary");
            System.out.println("------------------------------------------------------");
            
            while (resultSet.next()) {
                int id = resultSet.getInt("employee_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String department = resultSet.getString("department");
                double salary = resultSet.getDouble("salary");
                
                System.out.printf("%-3d %-10s %-10s %-25s %-15s $%.2f%n", 
                        id, firstName, lastName, email, department, salary);
            }
            System.out.println("------------------------------------------------------");
            
        } catch (SQLException e) {
            System.err.println("Database error occurred!");
            e.printStackTrace();
        } finally {
            // Step 5: Close all resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
