DROP DATABASE IF EXISTS nightfly;
CREATE DATABASE IF NOT EXISTS nightfly;

CREATE TABLE nightfly.batch (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    brewfather_id VARCHAR(50) UNIQUE,
    name VARCHAR(100),
    status VARCHAR(30)
    );