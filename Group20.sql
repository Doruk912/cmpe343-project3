DROP DATABASE IF EXISTS group20;
CREATE DATABASE group20;
USE group20;

CREATE TABLE users (
	id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('CASHIER', 'MANAGER', 'ADMIN') NOT NULL
);

INSERT INTO users (username, password, role) VALUES
('cashier1', '$2a$12$bg8YICXCIZEDOwkLH8r7aunb1cvwodIeXdeHSxIHxvEM5Ab4fSzXm', 'cashier'), -- Password: cashier1
('admin1', '$2a$12$8P4xr1QnXcpd3Rv1xDusSeWemcVPFKoeYXCSjAwVAZcvHABHdHZQi', 'admin'), -- Password: admin1
('manager1', '$2a$12$mJXc2Ta8vuApvOXdV1eDNuff.MqAZ5Gi8NNqNCjmV29.s14n.QVmC', 'manager'); -- Password: manager1