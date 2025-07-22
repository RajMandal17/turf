package examples;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This example demonstrates working with database and result set metadata
 */
public class MetadataExample {
    
    public static void main(String[] args) {
        Connection connection = null;
        
        try {
            // Establish connection
            connection = DatabaseUtil.getConnection();
            
            // Get database metadata
            System.out.println("DATABASE METADATA");
            System.out.println("=================");
            DatabaseMetaData dbmd = connection.getMetaData();
            
            // Display database information
            System.out.println("Database Product: " + dbmd.getDatabaseProductName() + " " + 
                    dbmd.getDatabaseProductVersion());
            System.out.println("JDBC Driver: " + dbmd.getDriverName() + " " + dbmd.getDriverVersion());
            System.out.println("JDBC URL: " + dbmd.getURL());
            System.out.println("Username: " + dbmd.getUserName());
            
            System.out.println("Supports transactions: " + dbmd.supportsTransactions());
            System.out.println("Supports batch updates: " + dbmd.supportsBatchUpdates());
            System.out.println("Supports stored procedures: " + dbmd.supportsStoredProcedures());
            
            // List all tables in the database
            System.out.println("\nTables in the database:");
            System.out.println("----------------------");
            
            ResultSet tables = dbmd.getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println(tableName);
                
                // Display columns in each table
                System.out.println("\tColumns:");
                ResultSet columns = dbmd.getColumns(null, null, tableName, "%");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String typeName = columns.getString("TYPE_NAME");
                    int columnSize = columns.getInt("COLUMN_SIZE");
                    boolean nullable = (columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                    
                    System.out.printf("\t- %s (%s, size: %d, nullable: %s)%n", 
                            columnName, typeName, columnSize, nullable);
                }
                System.out.println();
            }
            
            // Get result set metadata
            System.out.println("\nRESULT SET METADATA");
            System.out.println("==================");
            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employees");
            ResultSetMetaData rsmd = rs.getMetaData();
            
            // Display column information
            int columnCount = rsmd.getColumnCount();
            System.out.println("Query returns " + columnCount + " columns:");
            
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("Column %d: %s (%s, size: %d)%n",
                        i, 
                        rsmd.getColumnName(i), 
                        rsmd.getColumnTypeName(i), 
                        rsmd.getColumnDisplaySize(i));
            }
            
            // Clean up
            rs.close();
            stmt.close();
            tables.close();
            
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
