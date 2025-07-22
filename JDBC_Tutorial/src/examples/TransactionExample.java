package examples;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This example demonstrates transaction management in JDBC
 * It simulates a money transfer between two employees
 */
public class TransactionExample {
    
    public static void main(String[] args) {
        // Parameters for the transfer
        int fromEmployeeId = 1;
        int toEmployeeId = 2;
        double transferAmount = 5000.0;
        
        Connection connection = null;
        PreparedStatement checkStmt = null;
        PreparedStatement updateFromStmt = null;
        PreparedStatement updateToStmt = null;
        ResultSet resultSet = null;
        
        try {
            // Establish connection
            connection = DatabaseUtil.getConnection();
            
            // Print initial balances
            System.out.println("Initial salaries:");
            printEmployeeSalaries(connection, fromEmployeeId, toEmployeeId);
            
            // Start transaction - disable auto-commit mode
            connection.setAutoCommit(false);
            System.out.println("\nStarting transaction...");
            
            // Check if the source employee has enough salary
            checkStmt = connection.prepareStatement(
                    "SELECT salary FROM employees WHERE employee_id = ?");
            checkStmt.setInt(1, fromEmployeeId);
            resultSet = checkStmt.executeQuery();
            
            if (resultSet.next()) {
                double currentSalary = resultSet.getDouble("salary");
                
                if (currentSalary >= transferAmount) {
                    // Deduct from source employee
                    updateFromStmt = connection.prepareStatement(
                            "UPDATE employees SET salary = salary - ? WHERE employee_id = ?");
                    updateFromStmt.setDouble(1, transferAmount);
                    updateFromStmt.setInt(2, fromEmployeeId);
                    updateFromStmt.executeUpdate();
                    
                    System.out.println("Deducted $" + transferAmount + 
                            " from employee #" + fromEmployeeId);
                    
                    // Add to target employee
                    updateToStmt = connection.prepareStatement(
                            "UPDATE employees SET salary = salary + ? WHERE employee_id = ?");
                    updateToStmt.setDouble(1, transferAmount);
                    updateToStmt.setInt(2, toEmployeeId);
                    updateToStmt.executeUpdate();
                    
                    System.out.println("Added $" + transferAmount + 
                            " to employee #" + toEmployeeId);
                    
                    // Commit the transaction
                    connection.commit();
                    System.out.println("Transaction committed successfully!");
                } else {
                    System.out.println("Insufficient salary for transfer!");
                    connection.rollback();
                    System.out.println("Transaction rolled back!");
                }
            }
            
            // Print final balances
            System.out.println("\nFinal salaries:");
            printEmployeeSalaries(connection, fromEmployeeId, toEmployeeId);
            
        } catch (SQLException e) {
            System.err.println("Database error occurred!");
            e.printStackTrace();
            
            // Roll back the transaction on error
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Transaction rolled back due to error!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (checkStmt != null) checkStmt.close();
                if (updateFromStmt != null) updateFromStmt.close();
                if (updateToStmt != null) updateToStmt.close();
                
                // Restore auto-commit mode
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void printEmployeeSalaries(Connection conn, int emp1Id, int emp2Id) 
            throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = conn.prepareStatement(
                    "SELECT employee_id, first_name, last_name, salary " +
                    "FROM employees WHERE employee_id IN (?, ?)");
            pstmt.setInt(1, emp1Id);
            pstmt.setInt(2, emp2Id);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                System.out.printf("Employee #%d (%s %s): $%.2f%n",
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDouble("salary"));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
    }
}
