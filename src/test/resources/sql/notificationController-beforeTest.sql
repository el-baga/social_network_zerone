SELECT setval('persons_id_seq', 100, true);
SELECT setval('person_settings_id_seq', 100, true);
SELECT setval('notifications_id_seq', 100, true);

insert into person_settings (id, comment_comment, friend_birthday, friend_request, message, post, post_comment, post_like)
values (1, true, true, true, true, true, true, true),
       (2, true, true, true, true, true, true, true),
       (3, true, true, true, true, true, true, true);

insert into persons (id, first_name, last_name, e_mail, message_permissions, online_status, person_settings_id)
values (1, 'Name1', 'Surname1', '1@1.com', 'test message', false, 1),
       (2, 'Name2', 'Surname2', '2@2.com', 'test message', false, 2),
       (3, 'Name3', 'Surname3', '3@3.com', 'test message', false, 3);

insert into notifications (id, contact, entity_id, is_read, notification_type, sent_time, person_id, sender_id)
values (1, 'Name1 Surname1 отправил(а) вам заявку в друзья', 1, false, 'FRIEND_REQUEST', '2024-03-06 17:12:00.000000', 3, 1),
       (2, 'Name2 Surname2 опубликовал(а) новый пост', 2, false, 'POST', '2024-03-06 17:15:00.000000', 3, 2),
       (3, 'Name2 Surname2 опубликовал(а) новый пост', 2, true, 'POST', '2024-03-06 17:25:00.000000', 3, 2),
       (4, 'Name3 Surname3 прокомментировал(а) ваш пост', 3, true, 'POST_COMMENT', '2024-03-06 17:19:00.000000', 2, 3),
       (5, 'Surname2 Surname2 ответил(а) на ваш комментарий', 2, false, 'POST_COMMENT', '2024-03-06 17:38:00.000000', 3, 2);

