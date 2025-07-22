package advanced;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.mysql.cj.jdbc.MysqlDataSource;

/**
 * This example demonstrates basic connection pooling with MySQL's built-in DataSource
 * 
 * Note: In a real application, you would typically use a more robust connection pooling library
 * such as HikariCP, Apache DBCP, or C3P0
 */
public class ConnectionPoolingExample {
    
    private static MysqlDataSource dataSource;
    
    // Initialize the data source with connection pooling properties
    static {
        try {
            // Load database properties
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("resources/database.properties");
            props.load(fis);
            
            // Configure data source
            dataSource = new MysqlDataSource();
            dataSource.setURL(props.getProperty("db.url"));
            dataSource.setUser(props.getProperty("db.username"));
            dataSource.setPassword(props.getProperty("db.password"));
            try {
                dataSource.setCachePrepStmts(true);
                dataSource.setPrepStmtCacheSize(250);
                dataSource.setPrepStmtCacheSqlLimit(2048);
                dataSource.setUseServerPrepStmts(true);
            } catch (SQLException e) {
                System.err.println("Error configuring MysqlDataSource prepared statement cache.");
                e.printStackTrace();
            }
            
            // Set pool size (not supported by MysqlDataSource, requires a real pool like HikariCP)
            // int minPoolSize = Integer.parseInt(props.getProperty("db.min_pool_size", "5"));
            // int maxPoolSize = Integer.parseInt(props.getProperty("db.max_pool_size", "20"));
            // dataSource.setMaxPoolSize(maxPoolSize);
            // dataSource.setMaxIdleTime(Integer.parseInt(props.getProperty("db.max_idle_time", "300")));
            System.out.println("Connection pool initialized (MysqlDataSource does not support pool size directly)");
            
        } catch (IOException e) {
            System.err.println("Error loading properties file!");
            e.printStackTrace();
        }
    }
    
    // Get a connection from the pool
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public static void main(String[] args) {
        // Simulate multiple concurrent database operations
        int numThreads = 10;
        System.out.println("Starting " + numThreads + " concurrent database operations...");
        
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        // Submit tasks to executor
        for (int i = 1; i <= numThreads; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    performDatabaseOperation(taskId);
                } catch (SQLException e) {
                    System.err.println("Error in task " + taskId);
                    e.printStackTrace();
                }
            });
        }
        
        // Shutdown executor
        executor.shutdown();
        try {
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Tasks interrupted!");
            e.printStackTrace();
        }
        
        System.out.println("All database operations completed!");
    }
    
    private static void performDatabaseOperation(int taskId) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            System.out.println("Task " + taskId + ": Getting connection from pool...");
            connection = getConnection();
            
            // Simulate some database work
            System.out.println("Task " + taskId + ": Executing query...");
            statement = connection.createStatement();
            
            // Use different queries based on task ID to show variety
            String query;
            if (taskId % 3 == 0) {
                query = "SELECT COUNT(*) FROM employees";
            } else if (taskId % 3 == 1) {
                query = "SELECT AVG(salary) FROM employees";
            } else {
                query = "SELECT COUNT(*) FROM departments";
            }
            
            resultSet = statement.executeQuery(query);
            
            // Process result
            if (resultSet.next()) {
                System.out.println("Task " + taskId + ": Result = " + resultSet.getObject(1));
            }
            
            // Simulate some processing time
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
        } finally {
            // Return connection to pool by closing it
            System.out.println("Task " + taskId + ": Returning connection to pool...");
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }
}
