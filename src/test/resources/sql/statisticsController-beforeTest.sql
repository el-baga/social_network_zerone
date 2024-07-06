SELECT setval('persons_id_seq', 100, true);
SELECT setval('person_settings_id_seq', 100, true);
SELECT setval('posts_id_seq', 100, true);
SELECT setval('tags_id_seq', 100, true);
SELECT setval('post2tag_id_seq', 100, true);
SELECT setval('messages_id_seq', 100, true);
SELECT setval('dialogs_id_seq', 100, true);
SELECT setval('likes_id_seq', 100, true);
SELECT setval('post_comments_id_seq', 100, true);
SELECT setval('countries_id_seq', 100, true);
SELECT setval('cities_id_seq', 100, true);

insert into person_settings (id, comment_comment, friend_birthday, friend_request, message, post, post_comment, post_like)
values (1, true, true, true, true, true, true, true),
       (2, true, true, true, true, true, true, true),
       (3, true, true, true, true, true, true, true),
       (4, true, true, true, true, true, true, true),
       (5, true, true, true, true, true, true, true);

insert into persons (id, first_name, last_name, e_mail, message_permissions, online_status, person_settings_id, about, city, country, birth_date, is_blocked, last_online_time, reg_date, is_deleted, phone, photo, password)
values (1, 'Baga', 'Java', 'baga@mail.com', 'ALL', true, 1, 'about1', 'Moscow', 'Russia', '2003-01-01 00:00:00', false, '2024-02-09 15:10:00.000000', null, false, '79991123322', 'lizzard-ava.jpg', 'javaSE2024'),
       (2, 'Bob', 'Martin', 'alex@top.ru', 'ALL', false, 2, 'about2', 'Saint-Petersburg', 'Russia', '2002-10-22 00:00:00', false, '2024-02-09 15:10:00.000000', null, false, '79311659000', 'python-test.png', 'debug7'),
       (3, 'Yman', 'Back', 'ymanchik@back.ru', 'ALL', false, 3, 'about3', 'Sochi', 'Russia', '1990-11-11 00:00:00', false, '2024-02-09 15:10:00.000000', null, true, '78333124779', 'javaCrush.jpeg', 'backend45'),
       (4, 'Kris', 'Cook', 'kris@cook.ru', 'ALL', false, 4, 'about4', 'Saint-Petersburg', 'Russia', '2002-10-22 00:00:00', true, '2024-02-09 15:10:00.000000', null, false, '76898998999', 'zzz.png', 'dwwpad1234'),
       (5, 'Mark', 'Swarovski', 'sw_mark@mail.ru', 'ALL', false, 5, 'about5', 'Sochi', 'Russia', '1990-11-11 00:00:00', true, '2024-02-09 15:10:00.000000', null, false, '70012398761', 'mark.jpeg', 'wkwkwk2222');

insert into posts (id, title, post_text, time, is_blocked, is_deleted, time_delete, author_id)
values (1, 'title1', 'text1', '09/07/2023', false, false, null, 1),
       (2, 'title2', 'text2', '10/07/2023', false, false, null, 2);

insert into tags (id, tag)
values (1, 'tag1'),
       (2, 'tag2'),
       (3, 'tag3'),
       (4, 'tag4'),
       (5, 'tag5'),
       (6, 'tag6');

insert into post2tag (id, post_id, tag_id)
values (1, 1, 1),
       (2, 1, 2),
       (3, 1, 3),
       (4, 1, 4),
       (5, 2, 5),
       (6, 2, 6);

insert into dialogs (id, first_person_id, second_person_id, last_active_time, last_message_id)
values (1, 1, 2, '2024-02-09 15:00:00.000000', 1),
       (2, 1, 3, '2024-02-09 15:00:00.000000', 1);

insert into messages (id, is_deleted, message_text, read_status, time, dialog_id, author_id, recipient_id)
values (1, false, 'Text1', 'READ', '2024-02-09 15:00:00.000000', 1, 1, 2),
       (2, false, 'Text1', 'READ', '2024-02-09 15:00:00.000000', 1, 1, 2),
       (3, false, 'Text1', 'READ', '2024-02-09 15:00:00.000000', 1, 2, 1);

insert into post_comments (id, comment_text, is_blocked, is_deleted, time, post_id, author_id, parent_id)
values (1, 'POST COMMENT 1', false, false, '2024-02-09 15:10:00.000000', 1, 2, null),
       (2, 'POST COMMENT 2', false, false, '2024-02-09 15:11:00.000000', 1, 2, 1);

insert into likes (id, entity_id, time, type, person_id)
values (1, 1, '2024-01-30 13:46:41.531403', 'Post', 3),
       (2, 1, '2024-01-30 13:46:41.531403', 'Post', 1),
       (3, 1, '2024-01-30 13:46:41.531403', 'Post', 2),
       (4, 2, '2024-01-30 13:46:41.531403', 'Post', 4);

insert into countries (id, name, external_id)
values (20, 'Russia', 1),
       (21, 'Armenia', 2);

insert into cities (id, name, country_id)
values (1, 'Moscow', 20),
       (2, 'Saint-Petersburg', 20);