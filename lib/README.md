# MySQL JDBC Driver

Place the MySQL Connector/J JAR file in this directory.

## Download Instructions:

1. Go to: https://dev.mysql.com/downloads/connector/j/
2. Download the latest version (e.g., mysql-connector-java-8.0.33.jar)
3. Copy the JAR file to this directory

## Alternative Download via wget:

```bash
# Example command (update version as needed)
wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
```

The JAR file should be named something like:
- mysql-connector-java-8.0.33.jar
- mysql-connector-j-8.0.33.jar (newer versions)

Once placed here, the compile.sh and run.sh scripts will automatically include it in the classpath.
