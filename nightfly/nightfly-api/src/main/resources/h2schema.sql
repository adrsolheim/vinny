--DROP DATABASE IF EXISTS nightfly;
--CREATE DATABASE IF NOT EXISTS nightfly;

CREATE ALIAS IF NOT EXISTS JSON_VALUE FOR "no.vinny.nightfly.util.H2JsonFunctions.jsonValue";

DROP TABLE IF EXISTS tap;
DROP TABLE IF EXISTS batch_unit;
DROP TABLE IF EXISTS keg;
DROP TABLE IF EXISTS batch;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS sync_recipe;
DROP TABLE IF EXISTS sync_batch;

DROP TABLE IF EXISTS account;

CREATE TABLE IF NOT EXISTS recipe (
    id BIGINT NOT NULL AUTO_INCREMENT(100) PRIMARY KEY,
    brewfather_id VARCHAR(50) UNIQUE,
    name VARCHAR(100),
    updated TIMESTAMP
);

CREATE TABLE IF NOT EXISTS batch (
    id BIGINT NOT NULL AUTO_INCREMENT(100) PRIMARY KEY,
    brewfather_id VARCHAR(50) UNIQUE,
    name VARCHAR(100),
    status VARCHAR(30),
    recipe BIGINT,
    updated TIMESTAMP,
    FOREIGN KEY (recipe) REFERENCES recipe(id)
);

CREATE TABLE IF NOT EXISTS keg (
    id BIGINT NOT NULL AUTO_INCREMENT(100) PRIMARY KEY,
    capacity DECIMAL,
    brand VARCHAR(100),
    serial_number VARCHAR(255),
    purchase_condition VARCHAR(100),
    note VARCHAR (1000)
);

CREATE TABLE IF NOT EXISTS batch_unit (
    id BIGINT NOT NULL AUTO_INCREMENT(100) PRIMARY KEY,
    batch BIGINT,
    tap TINYINT,
    packaging VARCHAR(30),
    tap_status VARCHAR(30),
    volume_status VARCHAR(30),
    keg BIGINT,
    FOREIGN KEY (batch) REFERENCES batch(id),
    FOREIGN KEY (keg) REFERENCES keg(id)
);

CREATE TABLE IF NOT EXISTS tap (
    id TINYINT NOT NULL PRIMARY KEY,
    active BOOLEAN,
    batch_unit BIGINT,
    FOREIGN KEY (batch_unit) REFERENCES batch_unit(id)
);

CREATE TABLE IF NOT EXISTS account (
    id BIGINT NOT NULL AUTO_INCREMENT(100) PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS sync_recipe (
    id BIGINT NOT NULL AUTO_INCREMENT(100) PRIMARY KEY,
    updated_epoch BIGINT,
    brewfather_id VARCHAR(50),
    entity JSON
);

CREATE TABLE IF NOT EXISTS sync_batch (
    id BIGINT NOT NULL AUTO_INCREMENT(100) PRIMARY KEY,
    updated_epoch BIGINT,
    brewfather_id VARCHAR(50),
    entity JSON
);
