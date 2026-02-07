CREATE DATABASE IF NOT EXISTS authdb;

USE authdb;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'owner-event', 'client') NOT NULL
);

INSERT INTO users (email, password, role)
VALUES ('admin@admin', 'admin', 'admin')
ON DUPLICATE KEY UPDATE password='admin', role='admin';