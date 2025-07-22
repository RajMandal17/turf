# JDBC Tutorial Quick Start Guide

This quick start guide will help you get up and running with the JDBC tutorial examples quickly.

## 1. Download JDBC Driver

First, you need to download the appropriate JDBC driver for your database:

### For MySQL:
Download MySQL Connector/J from: https://dev.mysql.com/downloads/connector/j/

### For MariaDB:
Download MariaDB Connector/J from: https://mariadb.com/downloads/connectors/

Copy the downloaded JAR file to the `lib/` directory in this project.

## 2. Configure Database

Edit the `resources/database.properties` file to match your database settings:

```properties
# Database configuration
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/jdbc_tutorial
db.username=your_username
db.password=your_password
db.min_pool_size=5
db.max_pool_size=20
db.max_idle_time=300
```

## 3. Create Database

Run the included SQL script to create the tutorial database:

```bash
mysql -u your_username -p < resources/setup_database.sql
```

Or using MariaDB:

```bash
mariadb -u your_username -p < resources/setup_database.sql
```

## 4. Compile Examples

Compile all the examples with:

```bash
./compile.sh
```

## 5. Run Your First Example

Run the basic JDBC example:

```bash
./run.sh BasicJdbcExample
```

## 6. What to Explore Next

Once you've run the basic example, try running:

1. `PreparedStatementExample` - Learn how to use parameterized queries
2. `TransactionExample` - See how JDBC handles transactions
3. `BatchProcessingExample` - Learn efficient batch operations

## 7. Learning Resources

For complete understanding of JDBC, read the detailed guide:
`notes/JDBC_Complete_Guide.md`

Enjoy learning JDBC!
