--DROP DATABASE IF EXISTS nightfly;
--CREATE DATABASE IF NOT EXISTS nightfly;
DROP TABLE IF EXISTS tap;
DROP TABLE IF EXISTS batch_unit;
DROP TABLE IF EXISTS keg;
DROP TABLE IF EXISTS batch;
DROP TABLE IF EXISTS recipe;

DROP TABLE IF EXISTS account;

CREATE TABLE IF NOT EXISTS recipe (
    id BIGINT NOT NULL AUTO_INCREMENT(100) PRIMARY KEY,
    brewfather_id VARCHAR(50) UNIQUE,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS batch (
    id BIGINT NOT NULL AUTO_INCREMENT(100) PRIMARY KEY,
    brewfather_id VARCHAR(50) UNIQUE,
    name VARCHAR(100),
    status VARCHAR(30),
    recipe BIGINT,
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
    packaging VARCHAR(30),
    tap_status VARCHAR(30),
    volume_status VARCHAR(30),
    keg BIGINT,
    FOREIGN KEY (batch) REFERENCES batch(id),
    FOREIGN KEY (keg) REFERENCES keg(id)
);

CREATE TABLE IF NOT EXISTS tap (
    id TINYINT NOT NULL PRIMARY KEY,
    batch BIGINT,
    FOREIGN KEY (batch) REFERENCES batch(id)
);

CREATE TABLE IF NOT EXISTS account (
    id BIGINT NOT NULL AUTO_INCREMENT(100) PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(255)
);


INSERT INTO recipe (id, brewfather_id, name) VALUES (100001, 'b1W72a2OuL9xsC1c1OTrgog6JiPP0b', 'Eldon');
INSERT INTO recipe (id, brewfather_id, name) VALUES (100002, 'oCJlXhR1iIdYgLMwem0V0mmYNBIIia', 'KIWI Pilsner');

INSERT INTO batch (id, brewfather_id, name, status, tap_status, packaging, recipe, tap) VALUES (10, 'LAXI2KWZXcU2pBpzrfg6B3Uy5940vQ', 'Eldon',            'COMPLETED', 'SERVING', 'KEG', 100001, 1);
INSERT INTO batch (id, brewfather_id, name, status, tap_status, packaging, recipe, tap) VALUES (11, '6TY4Hg2JfQA0zBnscwspPsVF29JAKM', 'KIWI Pilsner',     'COMPLETED', 'SERVING', 'KEG', 100002, 2);
INSERT INTO batch (id, brewfather_id, name, status, tap_status, packaging)              VALUES (12, 'HY27A73dYWZNMxapgE4UdljPtNvDOL', 'MarisOtter SMASH', 'COMPLETED', 'WAITING', 'KEG');
INSERT INTO batch (id, brewfather_id, name, status, tap_status, packaging)              VALUES (13, 'NLltIcoo87foHbTz1N8rH0v0KXht6q', 'Juicy SafAle',     'COMPLETED', 'WAITING', 'KEG');
INSERT INTO batch (id, brewfather_id, name, status, tap_status, packaging)              VALUES (14, 'NLltIcoo87foHbTz1N8rH0v0KXht6P', 'Juicy NEIPA',      'COMPLETED', 'WAITING', 'KEG');
INSERT INTO batch (id, brewfather_id, name, status, tap_status, packaging)              VALUES (15, 'NLltIcoo87foHbTz1N8rH0v0KXht6A', 'NETTO',            'COMPLETED', 'WAITING', 'KEG');

INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (1,  23,  'AEB',     null, 'STORE', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (2,  23,  'AEB',     null, 'STORE', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (3,  19,  'AEB',     null, 'STORE', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (4,  19,  'AEB',     null, 'STORE', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (5,  19,  'AEB',     null, 'STORE', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (6,  19,  'KEGLAND', null, 'STORE', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (7,  19,  'KEGLAND', null, 'STORE', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (8,  12,  'KEGLAND', null, 'STORE', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (9,  9.5, 'KEGLAND', null, 'STORE', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (10, 9.5, 'UNKNOWN', null, 'USED', 'Fat fra utlandet / brusfat');
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (11, 9.5, 'UNKNOWN', null, 'USED', 'Fat fra utlandet / brusfat');
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (12, 9.5, 'UNKNOWN', null, 'USED', 'Fat fra utlandet / brusfat');

INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (1, 10, 'CONNECTED',    'KEG', 'NOT_EMPTY', 1);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (2, 10, 'WAITING',      'KEG', 'NOT_EMPTY', 2);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (3, 11, 'SERVING',      'KEG', 'NOT_EMPTY', 3);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (4, 11, 'DISCONNECTED', 'KEG', 'EMPTY',     4);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (5, 12, 'DISCONNECTED', 'KEG', 'EMPTY',     5);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (6, 12, 'DISCONNECTED', 'KEG', 'NOT_EMPTY', 6);

INSERT INTO tap (id, batch) VALUES (1, 10);
INSERT INTO tap (id, batch) VALUES (2, 11);
INSERT INTO tap (id) VALUES (3);
INSERT INTO tap (id) VALUES (4);

INSERT INTO account (username, password) VALUES ('', '');

