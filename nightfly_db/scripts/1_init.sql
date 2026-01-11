CREATE DATABASE IF NOT EXISTS nightfly;

CREATE TABLE IF NOT EXISTS nightfly.recipe (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    brewfather_id VARCHAR(50) UNIQUE,
    name VARCHAR(100),
    updated TIMESTAMP
) AUTO_INCREMENT=100;

CREATE TABLE IF NOT EXISTS nightfly.batch (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    brewfather_id VARCHAR(50) UNIQUE,
    name VARCHAR(100),
    status VARCHAR(30),
    recipe BIGINT,
    updated TIMESTAMP,
    FOREIGN KEY (recipe) REFERENCES recipe(id)
) AUTO_INCREMENT=100;

CREATE TABLE IF NOT EXISTS nightfly.keg (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    capacity DECIMAL,
    brand VARCHAR(100),
    serial_number VARCHAR(255),
    purchase_condition VARCHAR(100),
    note VARCHAR (1000)
) AUTO_INCREMENT=100;

CREATE TABLE IF NOT EXISTS nightfly.batch_unit (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    batch BIGINT,
    tap TINYINT,
    packaging VARCHAR(30),
    tap_status VARCHAR(30),
    volume_status VARCHAR(30),
    keg BIGINT,
    FOREIGN KEY (batch) REFERENCES batch(id),
    FOREIGN KEY (keg) REFERENCES keg(id)
) AUTO_INCREMENT=100;

CREATE TABLE IF NOT EXISTS nightfly.tap (
    id TINYINT NOT NULL PRIMARY KEY,
    active BOOLEAN,
    batch_unit BIGINT,
    FOREIGN KEY (batch_unit) REFERENCES batch_unit(id)
);

CREATE TABLE IF NOT EXISTS nightfly.sync_recipe (
    id BIGINT NOT NULL AUTO_INCREMENT(100) PRIMARY KEY,
    updated_epoch BIGINT,
    brewfather_id VARCHAR(50),
    synced TIMESTAMP,
    entity JSON
);

CREATE TABLE IF NOT EXISTS nightfly.sync_batch (
    id BIGINT NOT NULL AUTO_INCREMENT(100) PRIMARY KEY,
    updated_epoch BIGINT,
    brewfather_id VARCHAR(50),
    synced TIMESTAMP,
    entity JSON
);
