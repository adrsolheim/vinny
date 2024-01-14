INSERT INTO recipe (id, brewfather_id, name) VALUES (100001, 'b1W72a2OuL9xsC1c1OTrgog6JiPP0b', 'Eldon');
INSERT INTO recipe (id, brewfather_id, name) VALUES (100002, 'oCJlXhR1iIdYgLMwem0V0mmYNBIIia', 'KIWI Pilsner');

INSERT INTO batch (id, brewfather_id, name, status, recipe) VALUES (10, 'LAXI2KWZXcU2pBpzrfg6B3Uy5940vQ', 'Eldon',            'COMPLETED', 100001);
INSERT INTO batch (id, brewfather_id, name, status, recipe) VALUES (11, '6TY4Hg2JfQA0zBnscwspPsVF29JAKM', 'KIWI Pilsner',     'COMPLETED', 100002);
INSERT INTO batch (id, brewfather_id, name, status)         VALUES (12, 'HY27A73dYWZNMxapgE4UdljPtNvDOL', 'MarisOtter SMASH', 'COMPLETED');
INSERT INTO batch (id, brewfather_id, name, status)         VALUES (13, 'NLltIcoo87foHbTz1N8rH0v0KXht6q', 'Juicy SafAle',     'COMPLETED');
INSERT INTO batch (id, brewfather_id, name, status)         VALUES (14, 'NLltIcoo87foHbTz1N8rH0v0KXht6P', 'Juicy NEIPA',      'COMPLETED');
INSERT INTO batch (id, brewfather_id, name, status)         VALUES (15, 'NLltIcoo87foHbTz1N8rH0v0KXht6A', 'NETTO',            'COMPLETED');


INSERT INTO keg (capacity, brand, serial_number, source, note) VALUES (23,  'AEB',     null, 'STORE', null);
INSERT INTO keg (capacity, brand, serial_number, source, note) VALUES (23,  'AEB',     null, 'STORE', null);
INSERT INTO keg (capacity, brand, serial_number, source, note) VALUES (19,  'AEB',     null, 'STORE', null);
INSERT INTO keg (capacity, brand, serial_number, source, note) VALUES (19,  'AEB',     null, 'STORE', null);
INSERT INTO keg (capacity, brand, serial_number, source, note) VALUES (19,  'AEB',     null, 'STORE', null);
INSERT INTO keg (capacity, brand, serial_number, source, note) VALUES (19,  'KEGLAND', null, 'STORE', null);
INSERT INTO keg (capacity, brand, serial_number, source, note) VALUES (19,  'KEGLAND', null, 'STORE', null);
INSERT INTO keg (capacity, brand, serial_number, source, note) VALUES (12,  'KEGLAND', null, 'STORE', null);
INSERT INTO keg (capacity, brand, serial_number, source, note) VALUES (9.5, 'KEGLAND', null, 'STORE', null);
INSERT INTO keg (capacity, brand, serial_number, source, note) VALUES (9.5, 'UNKNOWN', null, 'USED', 'Fat fra utlandet / brusfat');
INSERT INTO keg (capacity, brand, serial_number, source, note) VALUES (9.5, 'UNKNOWN', null, 'USED', 'Fat fra utlandet / brusfat');
INSERT INTO keg (capacity, brand, serial_number, source, note) VALUES (9.5, 'UNKNOWN', null, 'USED', 'Fat fra utlandet / brusfat');

INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (1, 10, 'CONNECTED',    'KEG', 'NOT_EMPTY', 1);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (2, 10, 'WAITING',      'KEG', 'NOT_EMPTY', 2);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (3, 11, 'SERVING',      'KEG', 'NOT_EMPTY', 3);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (4, 11, 'DISCONNECTED', 'KEG', 'EMPTY',     4);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (5, 12, 'DISCONNECTED', 'KEG', 'EMPTY',     5);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (6, 12, 'DISCONNECTED', 'KEG', 'NOT_EMPTY', 6);


INSERT INTO tap (id, batch_unit) VALUES (1, 10);
INSERT INTO tap (id, batch_unit) VALUES (2, 11);
INSERT INTO tap (id) VALUES (3);
INSERT INTO tap (id) VALUES (4);
