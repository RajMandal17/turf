# Quick Setup Guide for Turf Management System

## Prerequisites Check

Before running the application, ensure you have:

✅ **Java JDK 8+**
```bash
java -version
javac -version
```

✅ **MariaDB Server Running**
```bash
# Check if MariaDB is running
sudo systemctl status mariadb
# Or
mysql -u root -p -e "SELECT 1"
```

## Quick Start (5 Minutes)

### Step 1: Download MariaDB JDBC Driver
```bash
cd lib
wget https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/3.1.4/mariadb-java-client-3.1.4.jar
cd ..
```

### Step 2: Setup Database
```bash
# Login to MariaDB and run the setup
mysql -u root -p < database_setup.sql
```

### Step 3: Configure Database Connection
Edit `src/com/turf/util/DatabaseUtil.java` if needed:
- Line 11: Update MariaDB password
- Line 10: Update username if not 'root'
- Line 9: Update database URL if different

### Step 4: Compile and Run
```bash
./compile.sh
./run.sh
```

## Test the Application

### Login as Admin:
- Email: `admin@turf.com`
- Password: `admin123`

### Login as Customer:
- Email: `john@customer.com` 
- Password: `customer123`

### Sample Test Flow:
1. Login as admin
2. Add a few turfs
3. Logout and register as a new customer
4. Login as customer
5. Book a turf
6. View booking history

## Troubleshooting

### MariaDB Connection Issues:
```bash
# Check MariaDB service
sudo systemctl restart mariadb

# Test connection
mysql -u root -p -e "SHOW DATABASES;"
```

### JDBC Driver Issues:
- Ensure `mariadb-java-client-*.jar` is in the `lib/` directory
- Check the JAR file name in compile.sh and run.sh

### Compilation Issues:
```bash
# Check Java installation
which java
which javac

# Verify JAVA_HOME
echo $JAVA_HOME
```

### Permission Issues:
```bash
chmod +x compile.sh run.sh
```

## Default Test Data

The system comes with:
- 1 Admin user
- 1 Sample customer  
- 3 Sample turfs
- No bookings (empty)

## Ready to Use!

Your Turf Management System is now ready for use. Enjoy managing your turfs! 🏟️

---

Need help? Check the full README.md for detailed documentation.
