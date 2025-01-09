DROP DATABASE IF EXISTS group20;
CREATE DATABASE group20;
USE group20;

CREATE TABLE users (
	id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
	name VARCHAR(30) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('CASHIER', 'MANAGER', 'ADMIN') NOT NULL
);

CREATE TABLE movies (
	id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    genre VARCHAR(20) NOT NULL,
    summary TEXT,
    poster VARCHAR(255)
);

CREATE TABLE products (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(25),
    price DECIMAL(10, 2),
    quantity INT UNSIGNED
);

INSERT INTO users (username, name, password, role) VALUES
('cashier1', 'John Doe', '$2a$12$bg8YICXCIZEDOwkLH8r7aunb1cvwodIeXdeHSxIHxvEM5Ab4fSzXm', 'cashier'), -- Password: cashier1
('admin1', 'Jane Smith', '$2a$12$8P4xr1QnXcpd3Rv1xDusSeWemcVPFKoeYXCSjAwVAZcvHABHdHZQi', 'admin'), -- Password: admin1
('manager1', 'Mary Johnson', '$2a$12$mJXc2Ta8vuApvOXdV1eDNuff.MqAZ5Gi8NNqNCjmV29.s14n.QVmC', 'manager'); -- Password: manager1

INSERT INTO movies (title, genre, summary, poster) VALUES
('Oppenheimer', 'History', 'A dramatization of the life story of J. Robert Oppenheimer, the physicist who had a large hand in the development of the atomic bombs that brought an end to World War II.', 'https://media-cache.cinematerial.com/p/500x/2afqhdxx/oppenheimer-movie-poster.jpg?v=1683305737'),
('The Shawshank Redemption', 'Drama', 'A banker convicted of uxoricide forms a friendship over a quarter century with a hardened convict, while maintaining his innocence and trying to remain hopeful through simple compassion.', 'https://media-cache.cinematerial.com/p/500x/b5v2e9jg/the-shawshank-redemption-movie-poster.jpg?v=1596989012'),
('The Good, the Bad and the Ugly', 'Western', 'A bounty-hunting scam joins two men in an uneasy alliance against a third in a race to find a fortune in gold buried in a remote cemetery.', 'https://media-cache.cinematerial.com/p/500x/kq1viu08/il-buono-il-brutto-il-cattivo-movie-poster.jpg?v=1456308982'),
('The Matrix', 'Sci-Fi', 'When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence.', 'https://media-cache.cinematerial.com/p/500x/sejtqyfp/the-matrix-movie-poster.jpg?v=1673723611');

INSERT INTO products (name, price, quantity) VALUES
('Popcorn - Large', 99.99 , 250),
('Popcorn - Medium', 79.99, 250),
('Popcorn - Small', 59.99, 250),
('Nachos - Large', 250, 100),
('Nachos - Medium', 200, 100),
('Nachos - Small', 150, 100),
('Coca-Cola', 50, 500),
('Sprite', 50, 500),
('3D Glasses', 9.50, 1000);