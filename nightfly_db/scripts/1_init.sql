DROP DATABASE IF EXISTS nightfly;
CREATE DATABASE IF NOT EXISTS nightfly;

CREATE TABLE IF NOT EXISTS nightfly.recipe (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    brewfather_id VARCHAR(50) UNIQUE,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS nightfly.batch (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    brewfather_id VARCHAR(50) UNIQUE,
    name VARCHAR(100),
    status VARCHAR(30),
    tap_status VARCHAR(30),
    packaging VARCHAR(20),
    recipe BIGINT,
    tap TINYINT,
    FOREIGN KEY (recipe) REFERENCES recipe(id)
);

CREATE TABLE IF NOT EXISTS nightfly.tap (
    id TINYINT NOT NULL PRIMARY KEY,
    batch BIGINT,
    FOREIGN KEY (batch) REFERENCES batch(id)
);

CREATE TABLE IF NOT EXISTS nightfly.keg (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    capacity DECIMAL,
    brand VARCHAR(100),
    serial_number VARCHAR(255),
    source VARCHAR(100),
    note VARCHAR (1000)
);
