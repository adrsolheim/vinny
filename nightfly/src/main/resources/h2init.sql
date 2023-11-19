--DROP DATABASE IF EXISTS nightfly;
--CREATE DATABASE IF NOT EXISTS nightfly;
DROP TABLE IF EXISTS batch;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS tap;

CREATE TABLE IF NOT EXISTS tap (
    id TINYINT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS recipe (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    brewfather_id VARCHAR(50) UNIQUE,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS batch (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    brewfather_id VARCHAR(50) UNIQUE,
    name VARCHAR(100),
    status VARCHAR(30),
    tap_status VARCHAR(30),
    packaging VARCHAR(20),
    recipe BIGINT,
    tap TINYINT,
    FOREIGN KEY (recipe) REFERENCES recipe(id),
    FOREIGN KEY (tap) REFERENCES tap(id)
);

INSERT INTO tap (id) VALUES (1);
INSERT INTO tap (id) VALUES (2);
INSERT INTO tap (id) VALUES (3);
INSERT INTO tap (id) VALUES (4);

INSERT INTO recipe (id, brewfather_id, name) VALUES (100001, 'b1W72a2OuL9xsC1c1OTrgog6JiPP0b', 'Eldon');
INSERT INTO recipe (id, brewfather_id, name) VALUES (100002, 'oCJlXhR1iIdYgLMwem0V0mmYNBIIia', 'KIWI Pilsner');

INSERT INTO batch (brewfather_id, name, status, tap_status, packaging, recipe, tap) VALUES ('LAXI2KWZXcU2pBpzrfg6B3Uy5940vQ', 'Eldon',            'COMPLETED', 'SERVING', 'KEG', 100001, 1);
INSERT INTO batch (brewfather_id, name, status, tap_status, packaging, recipe, tap) VALUES ('6TY4Hg2JfQA0zBnscwspPsVF29JAKM', 'KIWI Pilsner',     'COMPLETED', 'SERVING', 'KEG', 100002, 2);
INSERT INTO batch (brewfather_id, name, status, tap_status, packaging)              VALUES ('HY27A73dYWZNMxapgE4UdljPtNvDOL', 'MarisOtter SMASH', 'COMPLETED', 'WAITING', 'KEG');
INSERT INTO batch (brewfather_id, name, status, tap_status, packaging)              VALUES ('NLltIcoo87foHbTz1N8rH0v0KXht6q', 'Juicy SafAle',     'COMPLETED', 'WAITING', 'KEG');
INSERT INTO batch (brewfather_id, name, status, tap_status, packaging)              VALUES ('NLltIcoo87foHbTz1N8rH0v0KXht6P', 'Juicy NEIPA',      'COMPLETED', 'WAITING', 'KEG');
INSERT INTO batch (brewfather_id, name, status, tap_status, packaging)              VALUES ('NLltIcoo87foHbTz1N8rH0v0KXht6A', 'NETTO',            'COMPLETED', 'WAITING', 'KEG');
