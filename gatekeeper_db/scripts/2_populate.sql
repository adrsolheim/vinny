INSERT INTO users (username, password, enabled) VALUES ('user', '$2a$10$KInja4YaK3iN6uS0SeuK9.ahGoA22WyMl4JJ4Fv1rGS7rU2nnXWSK', true);
INSERT INTO users (username, password, enabled) VALUES ('empty', '$2a$10$tolE2Hwi/YbjzpMhR/wv3O1JgOdZpCTCA2xCAab1P4/qQgXT1jVSa', true);

INSERT INTO authorities (username, authority) VALUES ('user', 'batches.write');
INSERT INTO authorities (username, authority) VALUES ('user', 'recipes.read');
INSERT INTO authorities (username, authority) VALUES ('user', 'recipes.write');
INSERT INTO authorities (username, authority) VALUES ('user', 'taps.read');
INSERT INTO authorities (username, authority) VALUES ('user', 'taps.write');
