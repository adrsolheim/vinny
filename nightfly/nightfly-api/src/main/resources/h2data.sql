SET @now_timestamp = NOW();

INSERT INTO recipe (id, brewfather_id, name, updated) VALUES (100001, 'b1W72a2OuL9xsC1c1OTrgog6JiPP0b', 'Eldon', @now_timestamp);
INSERT INTO recipe (id, brewfather_id, name, updated) VALUES (100002, 'oCJlXhR1iIdYgLMwem0V0mmYNBIIia', 'KIWI Pilsner', @now_timestamp);

INSERT INTO batch (id, brewfather_id, name, status, recipe, updated) VALUES (10, 'LAXI2KWZXcU2pBpzrfg6B3Uy5940vQ', 'Eldon',            'COMPLETED', 100001, @now_timestamp);
INSERT INTO batch (id, brewfather_id, name, status, recipe, updated) VALUES (11, '6TY4Hg2JfQA0zBnscwspPsVF29JAKM', 'KIWI Pilsner',     'COMPLETED', 100002, @now_timestamp);
INSERT INTO batch (id, brewfather_id, name, status, recipe, updated) VALUES (12, 'HY27A73dYWZNMxapgE4UdljPtNvDOL', 'MarisOtter SMASH', 'COMPLETED', null,   @now_timestamp);
INSERT INTO batch (id, brewfather_id, name, status, recipe, updated) VALUES (13, 'NLltIcoo87foHbTz1N8rH0v0KXht6q', 'Juicy SafAle',     'COMPLETED', null,   @now_timestamp);
INSERT INTO batch (id, brewfather_id, name, status, recipe, updated) VALUES (14, 'NLltIcoo87foHbTz1N8rH0v0KXht6P', 'Juicy NEIPA',      'COMPLETED', null,   @now_timestamp);
INSERT INTO batch (id, brewfather_id, name, status, recipe, updated) VALUES (15, 'NLltIcoo87foHbTz1N8rH0v0KXht6A', 'NETTO',            'COMPLETED', null,   @now_timestamp);

INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (1,  23,  'AEB',     null, 'NEW', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (2,  23,  'AEB',     null, 'NEW', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (3,  19,  'AEB',     null, 'NEW', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (4,  19,  'AEB',     null, 'NEW', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (5,  19,  'AEB',     null, 'NEW', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (6,  19,  'AEB',     null, 'USED', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (7,  19,  'AEB',     null, 'USED', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (8,  19,  'AEB',     null, 'USED', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (9,  19,  'AEB',     null, 'USED', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (10, 19,  'KEGLAND', null, 'NEW', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (11, 19,  'KEGLAND', null, 'NEW', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (12, 12,  'KEGLAND', null, 'NEW', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (13, 9.5, 'KEGLAND', null, 'NEW', null);
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (14, 9.5, 'UNKNOWN', null, 'USED', 'Fat fra utlandet / brusfat');
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (15, 9.5, 'UNKNOWN', null, 'USED', 'Fat fra utlandet / brusfat');
INSERT INTO keg (id, capacity, brand, serial_number, purchase_condition, note) VALUES (16, 9.5, 'UNKNOWN', null, 'USED', 'Fat fra utlandet / brusfat');

INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (1, 10, 'CONNECTED',    'KEG', 'NOT_EMPTY', 1);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (2, 10, 'CONNECTED',    'KEG', 'NOT_EMPTY', 2);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (3, 11, 'CONNECTED',    'KEG', 'NOT_EMPTY', 3);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (4, 11, 'DISCONNECTED', 'KEG', 'EMPTY',     4);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (5, 12, 'DISCONNECTED', 'KEG', 'EMPTY',     5);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (6, 12, 'CONNECTED',    'KEG', 'NOT_EMPTY', 6);
INSERT INTO batch_unit (id, batch, tap_status, packaging, volume_status, keg) VALUES (7, 12, 'DISCONNECTED', 'KEG', 'NOT_EMPTY', 7);

INSERT INTO keg_occupancy (keg_id, batch_unit_id, batch_id) VALUES (1, 1, 10);
INSERT INTO keg_occupancy (keg_id, batch_unit_id, batch_id) VALUES (2, 2, 10);
INSERT INTO keg_occupancy (keg_id, batch_unit_id, batch_id) VALUES (3, 3, 11);
INSERT INTO keg_occupancy (keg_id, batch_unit_id, batch_id) VALUES (6, 6, 12);
INSERT INTO keg_occupancy (keg_id, batch_unit_id, batch_id) VALUES (7, 7, 12);

INSERT INTO tap (id, active, batch_unit) VALUES (1, TRUE,  1);
INSERT INTO tap (id, active, batch_unit) VALUES (2, TRUE,  2);
INSERT INTO tap (id, active, batch_unit) VALUES (3, FALSE, 4);
INSERT INTO tap (id, active, batch_unit) VALUES (4, FALSE, 5);


