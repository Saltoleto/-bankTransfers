INSERT INTO suggestion_status (id, description ) VALUES (1, 'PENDING_PROCESSING');
INSERT INTO suggestion_status (id, description ) VALUES (2, 'PROCESSING');
INSERT INTO suggestion_status (id, description ) VALUES (3, 'APPROVED');
INSERT INTO bank_transfer_entity (id, counter_party_id, suggestion_status_id, date_transfer ) VALUES (1, '12345', 2, '2022-01-01');
INSERT INTO bank_transfer_entity (id, counter_party_id, suggestion_status_id, date_transfer ) VALUES (2, '12345', 1, '2022-01-02');
INSERT INTO bank_transfer_entity (id, counter_party_id, suggestion_status_id, date_transfer ) VALUES (3, '12345', 1, '2022-01-03');
INSERT INTO bank_transfer_entity (id, counter_party_id, suggestion_status_id, date_transfer ) VALUES (4, '12345', 1, '2022-01-04');
INSERT INTO bank_transfer_entity (id, counter_party_id, suggestion_status_id, date_transfer ) VALUES (5, '12345', 1, '2022-01-05');
INSERT INTO bank_transfer_entity (id, counter_party_id, suggestion_status_id, date_transfer ) VALUES (6, '12345', 1, '2022-01-06');