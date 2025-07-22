package advanced;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import examples.DatabaseUtil;

/**
 * This example demonstrates advanced ResultSet capabilities
 * including scrollable and updatable result sets
 */
public class AdvancedResultSetExample {
    
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            // Establish connection
            connection = DatabaseUtil.getConnection();
            
            System.out.println("ADVANCED RESULT SET EXAMPLE");
            System.out.println("==========================");
            
            // Create a scrollable, updatable ResultSet
            pstmt = connection.prepareStatement(
                    "SELECT employee_id, first_name, last_name, email, department, salary FROM employees",
                    ResultSet.TYPE_SCROLL_SENSITIVE,  // Allows scrolling back and forth
                    ResultSet.CONCUR_UPDATABLE        // Allows updates to the result set
            );
            
            rs = pstmt.executeQuery();
            
            // 1. Navigate through the ResultSet using various movement methods
            System.out.println("1. Demonstrating ResultSet navigation:");
            
            // Count total rows
            rs.last();  // Move to the last row
            int totalRows = rs.getRow();
            System.out.println("Total rows: " + totalRows);
            
            // Go back to first row
            rs.first();
            System.out.println("\nFirst row: " + rs.getString("first_name") + " " + rs.getString("last_name"));
            
            // Move to specific row (absolute position)
            rs.absolute(3);
            System.out.println("Row #3: " + rs.getString("first_name") + " " + rs.getString("last_name"));
            
            // Relative movement
            rs.relative(2);  // Move 2 rows forward
            System.out.println("After relative(2): " + rs.getString("first_name") + " " + rs.getString("last_name"));
            
            rs.relative(-1);  // Move 1 row backward
            System.out.println("After relative(-1): " + rs.getString("first_name") + " " + rs.getString("last_name"));
            
            // Last row
            rs.last();
            System.out.println("Last row: " + rs.getString("first_name") + " " + rs.getString("last_name"));
            
            // 2. Update data directly in the ResultSet
            System.out.println("\n2. Demonstrating ResultSet updates:");
            
            // Find a specific employee to update
            rs.beforeFirst();  // Reset position
            boolean found = false;
            
            while (rs.next()) {
                if (rs.getString("first_name").equals("John") && 
                        rs.getString("last_name").equals("Doe")) {
                    found = true;
                    
                    // Display current values
                    System.out.println("Found employee: " + rs.getString("first_name") + " " + 
                            rs.getString("last_name"));
                    System.out.println("Current salary: $" + rs.getDouble("salary"));
                    
                    // Update the salary directly in the ResultSet
                    double newSalary = rs.getDouble("salary") * 1.1;  // 10% raise
                    rs.updateDouble("salary", newSalary);
                    rs.updateRow();  // Commit the update
                    
                    System.out.println("Updated salary: $" + newSalary);
                    break;
                }
            }
            
            if (!found) {
                System.out.println("Employee 'John Doe' not found!");
            }
            
            // 3. Insert a new row through the ResultSet
            System.out.println("\n3. Demonstrating ResultSet insertion:");
            
            // Move to the insert row
            rs.moveToInsertRow();
            
            // Set values for the new employee
            rs.updateString("first_name", "Patricia");
            rs.updateString("last_name", "Martinez");
            rs.updateString("email", "patricia.martinez@example.com");
            rs.updateString("department", "Finance");
            rs.updateDouble("salary", 79000.00);
            
            // Insert the row
            rs.insertRow();
            
            System.out.println("New employee 'Patricia Martinez' inserted!");
            
            // Move back to the current row
            rs.moveToCurrentRow();
            
            // 4. Delete a row through the ResultSet
            System.out.println("\n4. Demonstrating ResultSet deletion:");
            
            // Find a specific employee to delete
            rs.beforeFirst();
            found = false;
            
            while (rs.next()) {
                if (rs.getString("email").equals("patricia.martinez@example.com")) {
                    found = true;
                    
                    System.out.println("Found employee to delete: " + 
                            rs.getString("first_name") + " " + rs.getString("last_name"));
                    
                    // Delete the row
                    rs.deleteRow();
                    
                    System.out.println("Employee deleted!");
                    break;
                }
            }
            
            if (!found) {
                System.out.println("Employee with email 'patricia.martinez@example.com' not found!");
            }
            
        } catch (SQLException e) {
            System.err.println("Database error occurred!");
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
