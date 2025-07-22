package examples;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for managing database connections
 */
public class DatabaseUtil {
    
    private static String dbDriver;
    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;
    
    // Static block to load properties when the class is first loaded
    static {
        try {
            // Load database properties from configuration file
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("resources/database.properties");
            props.load(fis);
            
            // Set database properties
            dbDriver = props.getProperty("db.driver");
            dbUrl = props.getProperty("db.url");
            dbUsername = props.getProperty("db.username");
            dbPassword = props.getProperty("db.password");
            
            // Load the JDBC driver
            Class.forName(dbDriver);
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get a database connection
     * @return a Connection object
     * @throws SQLException if a database error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }
    
    /**
     * Close a database connection safely
     * @param connection the Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
