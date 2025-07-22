# JDBC Tutorial Project

A comprehensive tutorial on Java Database Connectivity (JDBC), covering beginner to advanced concepts.

## Project Structure

- `notes/`: Detailed notes on JDBC concepts
  - `JDBC_Complete_Guide.md`: Complete JDBC tutorial from basics to advanced topics
  
- `src/`: Java source code
  - `examples/`: Basic JDBC examples
  - `advanced/`: Advanced JDBC examples
  
- `resources/`: Configuration files and SQL scripts
  - `database.properties`: Database configuration settings
  - `setup_database.sql`: SQL script to create the tutorial database
  
- `lib/`: External libraries (JDBC drivers)

## Setup Instructions

### Prerequisites

- JDK 8 or higher installed
- MySQL or MariaDB database server
- MySQL Connector/J or MariaDB JDBC driver

### Database Setup

1. Place the MySQL Connector/J or MariaDB JDBC driver JAR file in the `lib/` directory
2. Edit `resources/database.properties` to match your database settings
3. Run the SQL script to set up the database:

```bash
mysql -u youruser -p < resources/setup_database.sql
```

### Compiling and Running Examples

1. Compile all examples:

```bash
./compile.sh
```

2. Run a specific example:

```bash
./run.sh ExampleClassName
```

For example:

```bash
./run.sh BasicJdbcExample
```

## Example Classes

### Basic Examples

- `BasicJdbcExample`: Demonstrates basic JDBC connection and queries
- `PreparedStatementExample`: Shows how to use PreparedStatement to prevent SQL injection
- `TransactionExample`: Demonstrates transaction management
- `BatchProcessingExample`: Shows batch processing for multiple operations
- `MetadataExample`: Works with database and result set metadata
- `StoredProcedureExample`: Demonstrates calling stored procedures

### Advanced Examples

- `ConnectionPoolingExample`: Shows how to implement connection pooling
- `AdvancedResultSetExample`: Demonstrates scrollable and updatable result sets

## Learning Path

1. Start by reading the `notes/JDBC_Complete_Guide.md` file
2. Run the examples in the following order:
   - `BasicJdbcExample`
   - `PreparedStatementExample`
   - `TransactionExample`
   - `BatchProcessingExample`
   - `MetadataExample`
   - `StoredProcedureExample`
   - `AdvancedResultSetExample`
   - `ConnectionPoolingExample`
   
3. Examine the source code to understand the concepts
4. Modify the examples to deepen your understanding
5. Create your own JDBC applications

## Additional Resources

- [Java JDBC API Documentation](https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/)
- [MySQL Connector/J Documentation](https://dev.mysql.com/doc/connector-j/en/)
- [MariaDB JDBC Driver Documentation](https://mariadb.com/kb/en/mariadb-connector-j/)

## License

This tutorial project is provided for educational purposes.
