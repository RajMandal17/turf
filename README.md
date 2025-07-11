# Turf Management System

A complete Java console-based Turf Management System using JDBC and MySQL for booking and managing sports turfs.

## Features

### User Management
- **User Registration**: Register as Admin or Customer
- **User Authentication**: Login with email and password
- **Role-based Access**: Different menus for Admin and Customer users

### Admin Features
- **Turf Management**: Add, view, and delete turfs
- **Booking Overview**: View all bookings across all turfs
- **Full System Control**: Complete administrative access

### Customer Features
- **Turf Browsing**: View all available turfs with details
- **Booking System**: Book turfs with date, time, and duration
- **Booking History**: View personal booking history
- **Booking Cancellation**: Cancel future bookings

### System Features
- **Payment Calculation**: Automatic cost calculation (hourly_rate × duration)
- **Booking Conflicts**: Prevents double booking of turfs
- **Input Validation**: Comprehensive validation for all user inputs
- **Clean Interface**: Well-formatted console menus and displays

## Project Structure

```
turf/
├── src/
│   └── com/turf/
│       ├── App.java                 # Main application entry point
│       ├── model/                   # POJO classes
│       │   ├── User.java
│       │   ├── Turf.java
│       │   └── Booking.java
│       ├── dao/                     # Data Access Objects
│       │   ├── UserDAO.java
│       │   ├── TurfDAO.java
│       │   └── BookingDAO.java
│       ├── service/                 # Business Logic
│       │   ├── AuthService.java
│       │   ├── TurfService.java
│       │   └── BookingService.java
│       └── util/                    # Utilities
│           └── DatabaseUtil.java
├── lib/                            # JDBC Driver (place mysql-connector-java.jar here)
├── out/                            # Compiled classes (generated)
├── database_setup.sql              # Database setup script
├── compile.sh                      # Compilation script
├── run.sh                         # Run script
└── README.md                      # This file
```

## Database Schema

### Tables

#### users
```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'CUSTOMER') NOT NULL
);
```

#### turfs
```sql
CREATE TABLE turfs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(200) NOT NULL,
    hourly_rate DECIMAL(10,2) NOT NULL
);
```

#### bookings
```sql
CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    turf_id INT NOT NULL,
    booking_time DATETIME NOT NULL,
    duration INT NOT NULL,
    total_cost DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (turf_id) REFERENCES turfs(id) ON DELETE CASCADE
);
```

## Prerequisites

1. **Java Development Kit (JDK)** 8 or higher
2. **MySQL Server** 5.7 or higher
3. **MySQL Connector/J** (JDBC Driver)

## Setup Instructions

### 1. Database Setup

1. Install and start MySQL server
2. Create database and tables using the provided script:
   ```bash
   mysql -u root -p < database_setup.sql
   ```

### 2. JDBC Driver Setup

1. Download MySQL Connector/J from [MySQL Official Site](https://dev.mysql.com/downloads/connector/j/)
2. Create a `lib` directory in the project root
3. Copy `mysql-connector-java-x.x.x.jar` to the `lib` directory

### 3. Database Configuration

Update database connection details in `src/com/turf/util/DatabaseUtil.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/turf_management";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_mysql_password";
```

### 4. Compile and Run

Make scripts executable:
```bash
chmod +x compile.sh run.sh
```

Compile the project:
```bash
./compile.sh
```

Run the application:
```bash
./run.sh
```

## Usage Guide

### Default Login Credentials

**Admin:**
- Email: `admin@turf.com`
- Password: `admin123`

**Sample Customer:**
- Email: `john@customer.com`
- Password: `customer123`

### Sample Workflow

1. **Start Application**: Run `./run.sh`
2. **Login as Admin**: Use admin credentials
3. **Add Turfs**: Add some turfs with different rates
4. **Register Customer**: Create a new customer account
5. **Login as Customer**: Login with customer credentials
6. **Book Turf**: Select a turf and book it
7. **View Bookings**: Check booking history

### Input Formats

- **Date/Time**: `yyyy-MM-dd HH:mm` (e.g., `2025-07-15 14:30`)
- **Email**: Valid email format (e.g., `user@example.com`)
- **Duration**: Integer value in hours (1-12)
- **Hourly Rate**: Decimal number (e.g., `25.50`)

## Features in Detail

### Authentication & Authorization
- Secure login system with email and password
- Role-based menu access (Admin vs Customer)
- Session management throughout application lifecycle

### Turf Management
- Complete CRUD operations for turfs
- Validation for turf data (name, location, hourly rate)
- Conflict-free turf deletion (checks for existing bookings)

### Booking System
- Real-time availability checking
- Automatic cost calculation
- Future booking validation
- Booking conflict prevention
- Easy cancellation for future bookings

### Data Validation
- Email format validation
- Date/time format validation
- Positive number validation for rates and duration
- Input sanitization and error handling

## Error Handling

The application includes comprehensive error handling for:
- Database connection issues
- Invalid user inputs
- Booking conflicts
- Data validation failures
- SQL exceptions

## Future Enhancements

- Password encryption/hashing
- Email notifications for bookings
- Payment gateway integration
- Advanced booking search and filtering
- Report generation
- Multi-location support
- Mobile app integration

## Technologies Used

- **Language**: Java 8+
- **Database**: MySQL
- **JDBC**: Direct database connectivity
- **Architecture**: Layered (Model-DAO-Service-App)
- **Design Patterns**: DAO Pattern, Service Layer Pattern

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is created for educational purposes and is free to use and modify.

## Support

For issues or questions, please create an issue in the repository or contact the development team.

---

**Happy Coding! 🏟️⚽**