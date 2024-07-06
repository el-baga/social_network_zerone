insert into persons (id, first_name, last_name, e_mail, message_permissions, online_status)
values (1, 'Name1', 'Surname1', '1@1.com', 'test message', false);
insert into persons (id, first_name, last_name, e_mail, message_permissions, online_status)
values (2, 'Name2', 'Surname2', '2@2.com', 'test message', false);
insert into persons (id, first_name, last_name, e_mail, message_permissions, online_status)
values (3, 'Name3', 'Surname3', '3@3.com', 'test message', false);
insert into persons (id, first_name, last_name, e_mail, message_permissions, online_status)
values (4, 'Name4', 'Surname4', '4@4.com', 'test message', false);

SELECT setval ('persons_id_seq', (SELECT MAX(id) FROM persons));

insert into dialogs (id, first_person_id, second_person_id, last_active_time, last_message_id)
values (1, 1, 2, '2024-02-09 15:00:00.000000', 1);
insert into dialogs (id, first_person_id, second_person_id, last_active_time, last_message_id)
values (2, 1, 3, '2024-02-09 15:00:00.000000', 1);
insert into dialogs (id, first_person_id, second_person_id, last_active_time, last_message_id)
values (3, 3, 2, '2024-02-09 15:00:00.000000', 1);

SELECT setval ('dialogs_id_seq', (SELECT MAX(id) FROM dialogs));

insert into messages (id, is_deleted, message_text, read_status, time, dialog_id, author_id, recipient_id)
values (1, false, 'Text1', 'READ', '2024-02-09 15:00:00.000000', 1, 1, 2);
insert into messages (id, is_deleted, message_text, read_status, time, dialog_id, author_id, recipient_id)
values (2, false, 'Text2', 'READ', '2024-02-09 18:00:00.000000', 1, 1, 2);
insert into messages (id, is_deleted, message_text, read_status, time, dialog_id, author_id, recipient_id)
values (3, false, 'Text3', 'UNREAD', '2024-02-10 15:00:00.000000', 1, 2, 1);
insert into messages (id, is_deleted, message_text, read_status, time, dialog_id, author_id, recipient_id)
values (4, false, 'Text4', 'UNREAD', '2024-02-09 15:00:00.000000', 2, 1, 3);
insert into messages (id, is_deleted, message_text, read_status, time, dialog_id, author_id, recipient_id)
values (5, false, 'Text5', 'UNREAD', '2024-02-09 15:00:00.000000', 3, 3, 2);

SELECT setval ('messages_id_seq', (SELECT MAX(id) FROM messages));