INSERT INTO users (username, password, enabled) VALUES ('user', '$2a$10$KInja4YaK3iN6uS0SeuK9.ahGoA22WyMl4JJ4Fv1rGS7rU2nnXWSK', true);
INSERT INTO users (username, password, enabled) VALUES ('empty', '$2a$10$hRVMURGv7.xz4Ow2ruVLBeE0mMM6XFshctOeqBQ1VCc.41gp90zoi', true);

INSERT INTO authorities (username, authority) VALUES ('user', 'batches.read');
INSERT INTO authorities (username, authority) VALUES ('user', 'batches.write');
INSERT INTO authorities (username, authority) VALUES ('user', 'recipes.read');
INSERT INTO authorities (username, authority) VALUES ('user', 'recipes.write');
INSERT INTO authorities (username, authority) VALUES ('user', 'taps.read');
INSERT INTO authorities (username, authority) VALUES ('user', 'taps.write');

INSERT INTO authorities (username, authority) VALUES ('empty', 'batches.read');
INSERT INTO authorities (username, authority) VALUES ('empty', 'recipes.read');
INSERT INTO authorities (username, authority) VALUES ('empty', 'taps.read');
