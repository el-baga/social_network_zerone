insert into persons (id, first_name, last_name, e_mail, message_permissions, online_status)
values (1, 'Name1', 'Surname1', '1@1.com', 'test message', false);
insert into persons (id, first_name, last_name, e_mail, message_permissions, online_status)
values (2, 'Name2', 'Surname2', '2@2.com', 'test message', false);
insert into persons (id, first_name, last_name, e_mail, message_permissions, online_status)
values (3, 'Name3', 'Surname3', '3@3.com', 'test message', false);

SELECT setval ('persons_id_seq', (SELECT MAX(id) FROM persons));

insert into posts (id, title, post_text, time, is_blocked, is_deleted, time_delete, author_id)
values (1, 'title1', 'text1', '09/07/2023', false, false, null, 1);
insert into posts (id, title, post_text, time, is_blocked, is_deleted, time_delete, author_id)
values (2, 'title2', 'text2', '10/07/2021', false, false, null, 2);
insert into posts (id, title, post_text, time, is_blocked, is_deleted, time_delete, author_id)
values (3, 'title3', 'text3', '11/07/2021', false, false, null, 1);
insert into posts (id, title, post_text, time, is_blocked, is_deleted, time_delete, author_id)
values (4, 'title4', 'text4', '11/07/2026', false, false, null, 3);
insert into posts (id, title, post_text, time, is_blocked, is_deleted, time_delete, author_id)
values (5, 'title5', 'text5', '11/07/2021', true, false, null, 3);
insert into posts (id, title, post_text, time, is_blocked, is_deleted, time_delete, author_id)
values (6, 'title6', 'text6', '11/07/2021', false, true, null, 3);

SELECT setval ('posts_id_seq', (SELECT MAX(id) FROM posts));

insert into tags (id, tag)
values (1, 'tag1');
insert into tags (id, tag)
values (2, 'tag2');

SELECT setval ('tags_id_seq', (SELECT MAX(id) FROM tags));

insert into post2tag (id, post_id, tag_id)
values (1, 1, 1);
insert into post2tag (id, post_id, tag_id)
values (2, 1, 2);
insert into post2tag (id, post_id, tag_id)
values (3, 2, 1);

SELECT setval ('post2tag_id_seq', (SELECT MAX(id) FROM post2tag));