SELECT setval('persons_id_seq', 100, true);
SELECT setval('person_settings_id_seq', 100, true);
SELECT setval('posts_id_seq', 100, true);
SELECT setval('post_comments_id_seq', 100, true);
SELECT setval('likes_id_seq', 100, true);

insert into person_settings (id, comment_comment, friend_birthday, friend_request, message, post, post_comment, post_like)
values (1, true, true, true, true, true, true, true),
       (2, true, true, true, true, true, true, true),
       (3, true, true, true, true, true, true, true);

insert into persons (id, first_name, last_name, e_mail, message_permissions, online_status, person_settings_id)
values (1, 'Name1', 'Surname1', '1@1.com', 'test message', false, 1),
       (2, 'Name2', 'Surname2', '2@2.com', 'test message', false, 2),
       (3, 'Name3', 'Surname3', '3@3.com', 'test message', false, 3);

insert into posts (id, title, post_text, time, is_blocked, is_deleted, time_delete, author_id)
values (1, 'POST HEADER 1', 'POST TEXT 1', '2024-02-09 15:00:00.000000', false, false, null, 1),
       (2, 'POST HEADER 2', 'POST TEXT 2', '2024-02-09 15:10:00.000000', false, false, null, 1);

insert into post_comments (id, comment_text, is_blocked, is_deleted, time, post_id, author_id, parent_id)
values (1, 'POST COMMENT 1', false, false, '2024-02-09 15:10:00.000000', 1, 2, null),
       (2, 'POST COMMENT 2', false, false, '2024-02-09 15:11:00.000000', 1, 2, 1);

insert into likes (id, entity_id, time, type, person_id)
values (1, 1, '2024-01-30 13:46:41.531403', 'Post', 3);

