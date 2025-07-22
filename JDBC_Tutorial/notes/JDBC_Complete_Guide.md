# Complete Java JDBC Tutorial: Beginner to Advanced

## Table of Contents
1. [Introduction to JDBC](#introduction-to-jdbc)
2. [Setting Up Your JDBC Environment](#setting-up-your-jdbc-environment)
3. [JDBC Architecture](#jdbc-architecture)
4. [Basic JDBC Operations](#basic-jdbc-operations)
5. [Advanced JDBC Operations](#advanced-jdbc-operations)
6. [Best Practices](#best-practices)
7. [Common Pitfalls and Solutions](#common-pitfalls-and-solutions)
8. [JDBC in Production](#jdbc-in-production)

## Introduction to JDBC

### What is JDBC?
JDBC (Java Database Connectivity) is a Java API that allows Java programs to interact with relational databases. It provides a standard interface for connecting to databases, executing SQL queries, and processing results.

### Why Use JDBC?
- **Platform Independence**: Java's "write once, run anywhere" philosophy extends to database connectivity
- **Vendor Independence**: Switch database systems without changing code (with some limitations)
- **Rich Feature Set**: Supports advanced operations like stored procedures, batch updates, etc.
- **Integration**: Works seamlessly with Java enterprise technologies

### JDBC Versions History
- JDBC 1.0: Released with JDK 1.1 (1997)
- JDBC 2.0: Added with J2SE 1.2 (1998)
- JDBC 3.0: Introduced with J2SE 1.4 (2002)
- JDBC 4.0: Part of Java SE 6 (2006)
- JDBC 4.1: Included in Java SE 7 (2011)
- JDBC 4.2: Available in Java SE 8 (2014)
- JDBC 4.3: Released with Java SE 9 (2017)

## Setting Up Your JDBC Environment

### Required Components
1. **Java Development Kit (JDK)**: Version 8 or higher recommended
2. **JDBC Driver**: Driver specific to your database
3. **Database**: Any relational database (MySQL, PostgreSQL, Oracle, etc.)

### Types of JDBC Drivers
1. **Type 1 (JDBC-ODBC Bridge)**: 
   - Translates JDBC calls into ODBC calls
   - Limited functionality, performance issues
   - Deprecated since Java 8

2. **Type 2 (Native-API/partly Java driver)**:
   - Uses database-specific native client libraries
   - Better performance than Type 1
   - Platform-dependent

3. **Type 3 (Network-Protocol/pure Java driver)**:
   - Translates JDBC calls into database-independent network protocol
   - Requires middleware server
   - Good for managing multiple databases

4. **Type 4 (Thin Driver/pure Java driver)**:
   - Written entirely in Java
   - Converts JDBC calls directly into database-specific network protocol
   - Most commonly used driver type today
   - Platform-independent and offers good performance

### Setting Up MySQL JDBC Driver Example
1. Download the MySQL JDBC driver (Connector/J) from the MySQL website
2. Add the JAR file to your project's classpath
3. Use the following connection string:
```java
String url = "jdbc:mysql://localhost:3306/your_database";
String user = "username";
String password = "password";
Connection connection = DriverManager.getConnection(url, user, password);
```

## JDBC Architecture

### Key Components

1. **JDBC API**: The application programming interface
   - `java.sql` package: Core JDBC classes and interfaces
   - `javax.sql` package: Extended functionality (DataSource, connection pooling)

2. **JDBC Driver Manager**: 
   - Acts as a bridge between user applications and JDBC drivers
   - Loads appropriate drivers
   - Establishes database connections

3. **JDBC Drivers**: 
   - Database-specific implementations of JDBC interfaces
   - Handles translation between JDBC calls and database-specific protocols

4. **Connection**: 
   - Represents a session with a specific database
   - Used to create statements and manage transactions

### Core JDBC Interfaces

1. **Driver**: Interface implemented by database vendors
2. **Connection**: Session between Java application and database
3. **Statement**: Used to execute SQL queries
4. **PreparedStatement**: Precompiled SQL statement
5. **CallableStatement**: Used to execute stored procedures
6. **ResultSet**: Contains results of a query
7. **ResultSetMetaData**: Information about ResultSet data
8. **DatabaseMetaData**: Information about the database

## Basic JDBC Operations

### Establishing a Connection

```java
// 1. Load the driver (not needed since JDBC 4.0)
Class.forName("com.mysql.cj.jdbc.Driver");

// 2. Establish connection
String url = "jdbc:mysql://localhost:3306/mydatabase";
String username = "root";
String password = "password";
Connection connection = DriverManager.getConnection(url, username, password);

// 3. Close connection when done
connection.close();
```

### Executing Simple Queries

```java
// Create a statement
Statement statement = connection.createStatement();

// Execute a query
ResultSet resultSet = statement.executeQuery("SELECT * FROM employees");

// Process results
while (resultSet.next()) {
    int id = resultSet.getInt("employee_id");
    String name = resultSet.getString("employee_name");
    System.out.println(id + ": " + name);
}

// Close resources
resultSet.close();
statement.close();
```

### Using PreparedStatement

```java
// Create a prepared statement
String sql = "SELECT * FROM employees WHERE department = ? AND salary > ?";
PreparedStatement pstmt = connection.prepareStatement(sql);

// Set parameters
pstmt.setString(1, "Engineering");
pstmt.setDouble(2, 50000.0);

// Execute query
ResultSet resultSet = pstmt.executeQuery();

// Process results...
```

### Executing Updates

```java
// Using Statement
Statement statement = connection.createStatement();
int rowsAffected = statement.executeUpdate("UPDATE employees SET salary = salary * 1.1 WHERE department = 'Engineering'");

// Using PreparedStatement
String sql = "INSERT INTO employees (employee_name, department, salary) VALUES (?, ?, ?)";
PreparedStatement pstmt = connection.prepareStatement(sql);
pstmt.setString(1, "John Doe");
pstmt.setString(2, "Marketing");
pstmt.setDouble(3, 60000.0);
int rowsInserted = pstmt.executeUpdate();
```

## Advanced JDBC Operations

### Transaction Management

```java
try {
    // Disable auto-commit mode
    connection.setAutoCommit(false);
    
    // Perform multiple operations
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("UPDATE accounts SET balance = balance - 500 WHERE account_id = 1");
    stmt.executeUpdate("UPDATE accounts SET balance = balance + 500 WHERE account_id = 2");
    
    // Commit the transaction
    connection.commit();
} catch (SQLException e) {
    // Roll back the transaction if something goes wrong
    connection.rollback();
    e.printStackTrace();
} finally {
    // Restore auto-commit mode
    connection.setAutoCommit(true);
}
```

### Batch Processing

```java
PreparedStatement pstmt = connection.prepareStatement("INSERT INTO employees (name, dept, salary) VALUES (?, ?, ?)");

// Add multiple records to batch
for (Employee emp : employeeList) {
    pstmt.setString(1, emp.getName());
    pstmt.setString(2, emp.getDepartment());
    pstmt.setDouble(3, emp.getSalary());
    pstmt.addBatch();
}

// Execute batch
int[] updateCounts = pstmt.executeBatch();
```

### Handling BLOBs and CLOBs

```java
// Storing a BLOB (Binary Large Object)
String sql = "INSERT INTO documents (doc_name, doc_file) VALUES (?, ?)";
PreparedStatement pstmt = connection.prepareStatement(sql);
pstmt.setString(1, "Important Document");

// Create an input stream from a file
File file = new File("document.pdf");
FileInputStream fis = new FileInputStream(file);
pstmt.setBinaryStream(2, fis, (int) file.length());
pstmt.executeUpdate();

// Reading a BLOB
ResultSet rs = statement.executeQuery("SELECT doc_file FROM documents WHERE doc_id = 1");
if (rs.next()) {
    InputStream inputStream = rs.getBinaryStream("doc_file");
    // Process the input stream...
}
```

### Stored Procedures

```java
// Calling a stored procedure
CallableStatement cstmt = connection.prepareCall("{call GetEmployeesByDept(?)}");
cstmt.setString(1, "Engineering");
ResultSet rs = cstmt.executeQuery();

// Stored procedure with OUT parameter
CallableStatement cstmt2 = connection.prepareCall("{call CalculateBonus(?, ?)}");
cstmt2.setInt(1, employeeId);
cstmt2.registerOutParameter(2, Types.DECIMAL);
cstmt2.execute();
BigDecimal bonus = cstmt2.getBigDecimal(2);
```

### ResultSet Types and Concurrency

```java
// Create a scrollable, updatable ResultSet
Statement stmt = connection.createStatement(
    ResultSet.TYPE_SCROLL_SENSITIVE,  // Scrollable
    ResultSet.CONCUR_UPDATABLE        // Updatable
);

ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

// Move to a specific row
rs.absolute(5);  // Move to the 5th row

// Update the current row
rs.updateDouble("salary", 75000.0);
rs.updateRow();  // Commit the update

// Insert a new row
rs.moveToInsertRow();
rs.updateString("name", "Alice Smith");
rs.updateString("department", "Sales");
rs.updateDouble("salary", 65000.0);
rs.insertRow();
rs.moveToCurrentRow();
```

### Metadata

```java
// DatabaseMetaData
DatabaseMetaData dbmd = connection.getMetaData();
System.out.println("Database: " + dbmd.getDatabaseProductName() + " " + dbmd.getDatabaseProductVersion());
System.out.println("Driver: " + dbmd.getDriverName() + " " + dbmd.getDriverVersion());

// List all tables
ResultSet tables = dbmd.getTables(null, null, "%", new String[]{"TABLE"});
while (tables.next()) {
    System.out.println(tables.getString("TABLE_NAME"));
}

// ResultSetMetaData
Statement stmt = connection.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM employees");
ResultSetMetaData rsmd = rs.getMetaData();

int columnCount = rsmd.getColumnCount();
for (int i = 1; i <= columnCount; i++) {
    System.out.println("Column " + i + ": " + rsmd.getColumnName(i) + 
                      " (" + rsmd.getColumnTypeName(i) + ")");
}
```

## Best Practices

### Connection Pooling
Connection pooling is essential for applications that frequently connect to a database. Instead of creating and closing connections for each database operation, connection pooling maintains a cache of database connections.

```java
// Using a basic DataSource (javax.sql.DataSource)
MysqlDataSource dataSource = new MysqlDataSource();
dataSource.setURL("jdbc:mysql://localhost:3306/mydatabase");
dataSource.setUser("username");
dataSource.setPassword("password");

Connection conn = dataSource.getConnection();
// Use connection...
conn.close(); // Returns to pool instead of closing

// Using a connection pool like HikariCP
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:mysql://localhost:3306/mydatabase");
config.setUsername("username");
config.setPassword("password");
config.setMaximumPoolSize(10);

HikariDataSource dataSource = new HikariDataSource(config);
Connection conn = dataSource.getConnection();
// Use connection...
conn.close(); // Returns to pool
```

### Resource Management
Always close JDBC resources in the reverse order they were opened to prevent resource leaks.

```java
// Using try-with-resources (Java 7+)
try (
    Connection conn = DriverManager.getConnection(url, user, password);
    PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM employees");
    ResultSet rs = pstmt.executeQuery()
) {
    // Process results...
} // Resources automatically closed

// Traditional approach
Connection conn = null;
PreparedStatement pstmt = null;
ResultSet rs = null;
try {
    conn = DriverManager.getConnection(url, user, password);
    pstmt = conn.prepareStatement("SELECT * FROM employees");
    rs = pstmt.executeQuery();
    // Process results...
} catch (SQLException e) {
    e.printStackTrace();
} finally {
    if (rs != null) try { rs.close(); } catch (SQLException e) { /* ignored */ }
    if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* ignored */ }
    if (conn != null) try { conn.close(); } catch (SQLException e) { /* ignored */ }
}
```

### SQL Injection Prevention
Always use PreparedStatement instead of Statement when dealing with user input to prevent SQL injection.

```java
// BAD: Vulnerable to SQL injection
String userInput = "' OR 1=1; --"; // Malicious input
Statement stmt = connection.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + userInput + "'");

// GOOD: Using PreparedStatement
String userInput = "' OR 1=1; --"; // Malicious input is neutralized
PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
pstmt.setString(1, userInput);
ResultSet rs = pstmt.executeQuery();
```

### Batch Processing for Better Performance
Use batch processing for inserting or updating multiple records.

```java
PreparedStatement pstmt = connection.prepareStatement("INSERT INTO logs (message, timestamp) VALUES (?, ?)");

for (int i = 0; i < 1000; i++) {
    pstmt.setString(1, "Log message " + i);
    pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
    pstmt.addBatch();
    
    // Execute in batches of 100
    if (i % 100 == 0) {
        pstmt.executeBatch();
    }
}
pstmt.executeBatch(); // Don't forget remaining records
```

## Common Pitfalls and Solutions

### Connection Leaks
**Problem**: Not closing connections properly leads to connection leaks.
**Solution**: Always close connections in a finally block or use try-with-resources.

### ResultSet Navigation Issues
**Problem**: Attempting to access a ResultSet that has been exhausted or closed.
**Solution**: Check if ResultSet has rows with rs.next() before accessing data.

### Performance Issues
**Problem**: Slow queries or inefficient connection management.
**Solution**: 
- Use connection pooling
- Optimize SQL queries
- Use prepared statements
- Consider batch operations for multiple similar operations

### Database-Specific SQL
**Problem**: Using database-specific SQL features that aren't portable.
**Solution**: Stick to standard SQL features or isolate database-specific code.

## JDBC in Production

### Monitoring and Logging
- Use logging frameworks (SLF4J, Log4j) to track SQL operations
- Consider logging long-running queries for optimization
- Monitor connection pool usage

### Connection Pool Tuning
- Set appropriate pool size (typically 10-20 connections for small/medium apps)
- Configure timeout settings for idle connections
- Set up statement caching if available

### Testing JDBC Code
- Use in-memory databases like H2 for unit testing
- Mock JDBC connections for isolated testing
- Test with actual database for integration testing

### JDBC vs ORM
While JDBC gives you direct control over SQL, Object-Relational Mapping (ORM) frameworks like Hibernate provide:
- Automatic mapping between Java objects and database tables
- Caching and lazy loading
- Simpler query language (HQL, JPQL)
- Less boilerplate code

Consider using JPA (Java Persistence API) with Hibernate for complex applications, while keeping JDBC for simpler use cases or performance-critical operations.
