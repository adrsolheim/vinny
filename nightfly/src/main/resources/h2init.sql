--DROP DATABASE IF EXISTS nightfly;
--CREATE DATABASE IF NOT EXISTS nightfly;

CREATE TABLE batch (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    brewfather_id VARCHAR(50) UNIQUE,
    name VARCHAR(100),
    status VARCHAR(30)
    );


INSERT INTO batch (brewfather_id, name, status) VALUES ('HY27A73dYWZNMxapgE4UdljPtNvDOL', 'MarisOtter SMASH', 'COMPLETED');
INSERT INTO batch (brewfather_id, name, status) VALUES ('LAXI2KWZXcU2pBpzrfg6B3Uy5940vQ', 'Eldon', 'COMPLETED');
INSERT INTO batch (brewfather_id, name, status) VALUES ('NLltIcoo87foHbTz1N8rH0v0KXht6q', 'Juicy SafAle', 'COMPLETED');
INSERT INTO batch (brewfather_id, name, status) VALUES ('LAXI2KWZXcU2pBpzrfg6B3Uy5940vS', 'Kiwi', 'COMPLETED');
INSERT INTO batch (brewfather_id, name, status) VALUES ('NLltIcoo87foHbTz1N8rH0v0KXht6P', 'Juicy NEIPA', 'COMPLETED');
INSERT INTO batch (brewfather_id, name, status) VALUES ('NLltIcoo87foHbTz1N8rH0v0KXht6A', 'NETTO', 'COMPLETED');
