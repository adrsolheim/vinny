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
    recipe BIGINT,
    FOREIGN KEY (recipe) REFERENCES recipe(id)
);

CREATE TABLE IF NOT EXISTS nightfly.keg (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    capacity DECIMAL,
    brand VARCHAR(100),
    serial_number VARCHAR(255),
    purchase_condition VARCHAR(100),
    note VARCHAR (1000)
);

CREATE TABLE IF NOT EXISTS nightfly.batch_unit (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    batch BIGINT,
    packaging VARCHAR(30),
    tap_status VARCHAR(30),
    volume_status VARCHAR(30),
    keg BIGINT,
    FOREIGN KEY (batch) REFERENCES batch(id),
    FOREIGN KEY (keg) REFERENCES keg(id)
);

CREATE TABLE IF NOT EXISTS nightfly.tap (
    id TINYINT NOT NULL PRIMARY KEY,
    batch BIGINT,
    FOREIGN KEY (batch) REFERENCES batch(id)
);
