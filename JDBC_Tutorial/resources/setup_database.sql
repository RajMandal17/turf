-- Create JDBC Tutorial Database
CREATE DATABASE IF NOT EXISTS jdbc_tutorial;
USE jdbc_tutorial;

-- Create Employee Table
CREATE TABLE IF NOT EXISTS employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department VARCHAR(50) NOT NULL,
    salary DECIMAL(10,2) NOT NULL,
    hire_date DATE NOT NULL
);

-- Create Department Table
CREATE TABLE IF NOT EXISTS departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL
);

-- Create Projects Table
CREATE TABLE IF NOT EXISTS projects (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    budget DECIMAL(15,2) NOT NULL
);

-- Create Employee-Project Junction Table
CREATE TABLE IF NOT EXISTS employee_projects (
    employee_id INT NOT NULL,
    project_id INT NOT NULL,
    role VARCHAR(50) NOT NULL,
    join_date DATE NOT NULL,
    PRIMARY KEY (employee_id, project_id),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id),
    FOREIGN KEY (project_id) REFERENCES projects(project_id)
);

-- Insert Sample Data into Departments
INSERT INTO departments (department_name, location) VALUES
('Engineering', 'Building A'),
('Marketing', 'Building B'),
('Human Resources', 'Building A'),
('Finance', 'Building C'),
('Sales', 'Building B');

-- Insert Sample Data into Employees
INSERT INTO employees (first_name, last_name, email, department, salary, hire_date) VALUES
('John', 'Doe', 'john.doe@example.com', 'Engineering', 85000.00, '2020-01-15'),
('Jane', 'Smith', 'jane.smith@example.com', 'Marketing', 75000.00, '2020-03-20'),
('Michael', 'Johnson', 'michael.johnson@example.com', 'Engineering', 90000.00, '2019-11-10'),
('Emily', 'Williams', 'emily.williams@example.com', 'Human Resources', 65000.00, '2021-02-05'),
('Robert', 'Brown', 'robert.brown@example.com', 'Finance', 95000.00, '2018-07-12'),
('Sarah', 'Davis', 'sarah.davis@example.com', 'Engineering', 82000.00, '2020-09-30'),
('David', 'Miller', 'david.miller@example.com', 'Sales', 78000.00, '2021-04-18'),
('Jennifer', 'Wilson', 'jennifer.wilson@example.com', 'Marketing', 72000.00, '2020-06-22'),
('James', 'Taylor', 'james.taylor@example.com', 'Engineering', 88000.00, '2019-08-14'),
('Lisa', 'Anderson', 'lisa.anderson@example.com', 'Human Resources', 67000.00, '2021-01-08');

-- Insert Sample Data into Projects
INSERT INTO projects (project_name, start_date, end_date, budget) VALUES
('Website Redesign', '2021-01-01', '2021-04-30', 150000.00),
('Mobile App Development', '2021-02-15', '2021-08-31', 300000.00),
('Database Migration', '2021-03-10', '2021-06-15', 120000.00),
('Cloud Infrastructure', '2021-04-01', '2022-01-31', 500000.00),
('Marketing Campaign', '2021-05-15', '2021-11-30', 250000.00);

-- Assign Employees to Projects
INSERT INTO employee_projects (employee_id, project_id, role, join_date) VALUES
(1, 1, 'Developer', '2021-01-01'),
(3, 1, 'Lead Developer', '2021-01-01'),
(6, 1, 'Developer', '2021-01-15'),
(9, 1, 'Tester', '2021-02-01'),
(1, 2, 'Mobile Developer', '2021-02-15'),
(3, 2, 'Lead Developer', '2021-02-15'),
(9, 2, 'Tester', '2021-03-01'),
(5, 3, 'Database Administrator', '2021-03-10'),
(6, 3, 'Developer', '2021-03-10'),
(3, 4, 'System Architect', '2021-04-01'),
(5, 4, 'Financial Analyst', '2021-04-01'),
(2, 5, 'Marketing Specialist', '2021-05-15'),
(8, 5, 'Marketing Analyst', '2021-05-15'),
(7, 5, 'Sales Representative', '2021-06-01');

-- Create stored procedure to get employees by department
DELIMITER //
CREATE PROCEDURE GetEmployeesByDept(IN dept_name VARCHAR(50))
BEGIN
    SELECT * FROM employees WHERE department = dept_name;
END //
DELIMITER ;

-- Create stored procedure to calculate bonus based on employee's salary
DELIMITER //
CREATE PROCEDURE CalculateBonus(IN emp_id INT, OUT bonus DECIMAL(10,2))
BEGIN
    DECLARE emp_salary DECIMAL(10,2);
    DECLARE emp_years INT;
    
    -- Get employee salary and years of service
    SELECT salary, TIMESTAMPDIFF(YEAR, hire_date, CURDATE()) 
    INTO emp_salary, emp_years
    FROM employees WHERE employee_id = emp_id;
    
    -- Calculate bonus based on salary and years of service
    IF emp_years < 2 THEN
        SET bonus = emp_salary * 0.05;
    ELSEIF emp_years < 5 THEN
        SET bonus = emp_salary * 0.10;
    ELSE
        SET bonus = emp_salary * 0.15;
    END IF;
END //
DELIMITER ;
