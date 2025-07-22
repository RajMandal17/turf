package examples;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * This example demonstrates working with stored procedures in JDBC
 */
public class StoredProcedureExample {
    
    public static void main(String[] args) {
        Connection connection = null;
        
        try {
            // Establish connection
            connection = DatabaseUtil.getConnection();
            
            // Example 1: Stored procedure with a result set
            System.out.println("EXAMPLE 1: Calling GetEmployeesByDept");
            System.out.println("===================================");
            
            // Prepare the stored procedure call
            CallableStatement cstmt1 = connection.prepareCall("{call GetEmployeesByDept(?)}");
            
            // Set the input parameter
            cstmt1.setString(1, "Engineering");
            
            // Execute and get result set
            ResultSet rs = cstmt1.executeQuery();
            
            // Process the result set
            System.out.println("Engineering Department Employees:");
            System.out.println("-------------------------------");
            
            while (rs.next()) {
                int id = rs.getInt("employee_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                double salary = rs.getDouble("salary");
                
                System.out.printf("ID: %d, Name: %s %s, Salary: $%.2f%n", 
                        id, firstName, lastName, salary);
            }
            
            rs.close();
            cstmt1.close();
            
            // Example 2: Stored procedure with OUT parameter
            System.out.println("\nEXAMPLE 2: Calling CalculateBonus");
            System.out.println("================================");
            
            // Prepare the stored procedure call
            CallableStatement cstmt2 = connection.prepareCall("{call CalculateBonus(?, ?)}");
            
            // Loop through several employee IDs
            for (int empId = 1; empId <= 5; empId++) {
                // Set the input parameter
                cstmt2.setInt(1, empId);
                
                // Register the output parameter
                cstmt2.registerOutParameter(2, Types.DECIMAL);
                
                // Execute the stored procedure
                cstmt2.execute();
                
                // Get the output parameter value
                BigDecimal bonus = cstmt2.getBigDecimal(2);
                
                System.out.printf("Employee ID %d bonus: $%.2f%n", empId, bonus);
            }
            
            cstmt2.close();
            
        } catch (SQLException e) {
            System.err.println("Database error occurred!");
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
