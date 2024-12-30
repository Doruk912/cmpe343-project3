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
('cashier1', 'cashier1', 'cashier'),
('admin1', 'admin1', 'admin'),
('manager1', 'manager1', 'manager');