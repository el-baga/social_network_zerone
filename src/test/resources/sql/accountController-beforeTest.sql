SELECT setval('persons_id_seq', 100, true);
SELECT setval('person_settings_id_seq', 100, true);

insert into person_settings (id, comment_comment, friend_birthday, friend_request, message, post, post_comment, post_like)
values (1, true, true, true, true, true, true, true),
       (2, true, false, true, false, true, false, true),
       (3, true, true, true, true, true, true, true),
       (4, true, true, true, true, true, true, true);

insert into persons (id, first_name, last_name, e_mail, message_permissions, online_status, person_settings_id, password, change_password_token, email_uuid, email_uuid_time, change_password_token_time)
values (1, 'Name1', 'Surname1', '1@1.com', 'test message', false, 1, null, null, null, null, null),
       (2, 'Name2', 'Surname2', '2@2.com', 'test message', false, 2, null, null, null, null, null),
       (3, 'Name3', 'Surname3', '3@3.com', 'test message', false, 3, null, null, 'secretForEmail', null, null),
       (4, 'Name4', 'Surname4', '4@4.com', 'test message', false, 4, null, 'secretForPassword', null, null, null);