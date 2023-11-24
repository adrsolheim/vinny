INSERT INTO recipe (id, brewfather_id, name) VALUES (100001, 'b1W72a2OuL9xsC1c1OTrgog6JiPP0b', 'Eldon');
INSERT INTO recipe (id, brewfather_id, name) VALUES (100002, 'oCJlXhR1iIdYgLMwem0V0mmYNBIIia', 'KIWI Pilsner');

INSERT INTO batch (id, brewfather_id, name, status, tap_status, packaging, recipe, tap) VALUES (10, 'LAXI2KWZXcU2pBpzrfg6B3Uy5940vQ', 'Eldon',            'COMPLETED', 'SERVING', 'KEG', 100001, 1);
INSERT INTO batch (id, brewfather_id, name, status, tap_status, packaging, recipe, tap) VALUES (11, '6TY4Hg2JfQA0zBnscwspPsVF29JAKM', 'KIWI Pilsner',     'COMPLETED', 'SERVING', 'KEG', 100002, 2);
INSERT INTO batch (id, brewfather_id, name, status, tap_status, packaging)              VALUES (12, 'HY27A73dYWZNMxapgE4UdljPtNvDOL', 'MarisOtter SMASH', 'COMPLETED', 'WAITING', 'KEG');
INSERT INTO batch (id, brewfather_id, name, status, tap_status, packaging)              VALUES (13, 'NLltIcoo87foHbTz1N8rH0v0KXht6q', 'Juicy SafAle',     'COMPLETED', 'WAITING', 'KEG');
INSERT INTO batch (id, brewfather_id, name, status, tap_status, packaging)              VALUES (14, 'NLltIcoo87foHbTz1N8rH0v0KXht6P', 'Juicy NEIPA',      'COMPLETED', 'WAITING', 'KEG');
INSERT INTO batch (id, brewfather_id, name, status, tap_status, packaging)              VALUES (15, 'NLltIcoo87foHbTz1N8rH0v0KXht6A', 'NETTO',            'COMPLETED', 'WAITING', 'KEG');

INSERT INTO tap (id, batch) VALUES (1, 10);
INSERT INTO tap (id, batch) VALUES (2, 11);
INSERT INTO tap (id) VALUES (3);
INSERT INTO tap (id) VALUES (4);

