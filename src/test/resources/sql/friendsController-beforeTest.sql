SELECT setval('friendships_id_seq', 100, true);
SELECT setval('persons_id_seq', 100, true);
SELECT setval('person_settings_id_seq', 100, true);

insert into person_settings (id, comment_comment, friend_birthday, friend_request, message, post, post_comment, post_like)
values (23, true, true, true, true, true, true, true),
       (24, true, true, true, true, true, true, true),
       (25, true, true, true, true, true, true, true),
       (26, true, true, true, true, true, true, true),
       (27, true, true, true, true, true, true, true),
       (28, true, true, true, true, true, true, true),
       (29, true, true, true, true, true, true, true),
       (30, true, true, true, true, true, true, true),
       (31, true, true, true, true, true, true, true),
       (32, true, true, true, true, true, true, true);


insert into persons (id, first_name, last_name, e_mail, message_permissions, online_status, person_settings_id)
values (1, 'Name1', 'Surname1', '1@1.com', 'test message', false, 23),
       (2, 'Name2', 'Surname2', '2@2.com', 'test message', false, 24),
       (3, 'Name3', 'Surname3', '3@3.com', 'test message', false, 25),
       (4, 'Name4', 'Surname4', '4@4.com', 'test message', false, 26),
       (5, 'Name5', 'Surname5', '5@5.com', 'test message', false, 27),
       (6, 'Name6', 'Surname6', '6@6.com', 'test message', false, 28),
       (7, 'Name7', 'Surname7', '7@7.com', 'test message', false, 29),
       (20, 'Name20', 'Surname20', '20@20.com', 'test message', false, 30),
       (21, 'Name21', 'Surname21', '21@21.com', 'test message', false, 31),
       (22, 'Name22', 'Surname22', '22@22.com', 'test message', false, 32);

-- for delete testing
insert into friendships (id, sent_time, status_name, dst_person_id, src_person_id)
values (20, '01/01/2024', 'FRIEND', 4, 3);

-- for add friend testing
insert into friendships (id, sent_time, status_name, dst_person_id, src_person_id)
values (21, '02/01/2024', 'REQUEST', 6, 5);

insert into friendships (id, sent_time, status_name, dst_person_id, src_person_id)
values (22, '02/01/2024', 'RECEIVED_REQUEST', 5, 6);

-- for unblock friend testing
insert into friendships (id, sent_time, status_name, dst_person_id, src_person_id)
values (25, '04/01/2024', 'BLOCKED', 7, 1);

-- for testing getFriends
insert into friendships (id, sent_time, status_name, dst_person_id, src_person_id)
values (26, '01/01/2024', 'FRIEND', 21, 20);

insert into friendships (id, sent_time, status_name, dst_person_id, src_person_id)
values (27, '01/01/2024', 'FRIEND', 22, 20);

insert into friendships (id, sent_time, status_name, dst_person_id, src_person_id)
values (28, '01/01/2024', 'FRIEND', 20, 21);

insert into friendships (id, sent_time, status_name, dst_person_id, src_person_id)
values (29, '01/01/2024', 'FRIEND', 20, 22);

-- for friends recommendation test when user has no friends
insert into persons(id, first_name, last_name, e_mail, message_permissions, online_status, birth_date, city)
values (23, 'Name23', 'Surname23', '23@23.com', 'test message', false, '03-04-1998', 'Omsk'),
       (24, 'Name24', 'Surname24', '24@24.com', 'test message', false, '01-03-1998', 'Omsk'),
       (25, 'Name25', 'Surname25', '25@25.com', 'test message', false, '12-02-1999', 'Omsk'),
       (26, 'Name26', 'Surname26', '26@26.com', 'test message', false, '03-04-2000', 'Omsk');

--for friends recommendation test when user has friends
insert into persons(id, first_name, last_name, e_mail, message_permissions, online_status, birth_date, city)
values (27, 'Name27', 'Surname27', '27@27.com', 'test message', false, '03-04-1998', 'Moscow'),
       (28, 'Name28', 'Surname28', '28@28.com', 'test message', false, '01-03-1998', 'Moscow'),
       (29, 'Name29', 'Surname29', '29@29.com', 'test message', false, '12-02-1999', 'Moscow'),
       (30, 'Name30', 'Surname30', '30@30.com', 'test message', false, '03-04-2000', 'Moscow'),
       (31, 'Name31', 'Surname31', '31@31.com', 'test message', false, '03-04-2000', 'Moscow'),
       (32, 'Name32', 'Surname32', '32@32.com', 'test message', false, '03-04-2000', 'Moscow'),
       (33, 'Name33', 'Surname33', '33@33.com', 'test message', false, '03-04-2000', 'Moscow'),
       (34, 'Name34', 'Surname34', '34@34.com', 'test message', false, '03-04-2000', 'Moscow'),
       (35, 'Name35', 'Surname35', '35@35.com', 'test message', false, '03-04-2000', 'Moscow'),
       (36, 'Name36', 'Surname36', '36@36.com', 'test message', false, '03-04-2000', 'Moscow'),
       (37, 'Name37', 'Surname37', '37@37.com', 'test message', false, '03-04-2000', 'Moscow'),
       (38, 'Name38', 'Surname38', '38@38.com', 'test message', false, '03-04-2000', 'Moscow')
ON CONFLICT (id) DO UPDATE
    SET first_name          = EXCLUDED.first_name,
        last_name           = EXCLUDED.last_name,
        e_mail              = EXCLUDED.e_mail,
        message_permissions = EXCLUDED.message_permissions,
        online_status       = EXCLUDED.online_status,
        birth_date          = EXCLUDED.birth_date,
        city                = EXCLUDED.city;

INSERT INTO friendships (id, sent_time, status_name, dst_person_id, src_person_id)
VALUES (30, '02/01/2024', 'FRIEND', 28, 27),
       (31, '02/01/2024', 'FRIEND', 27, 28),
       (32, '02/01/2024', 'FRIEND', 29, 28),
       (33, '02/01/2024', 'FRIEND', 28, 29),
       (34, '02/01/2024', 'FRIEND', 30, 28),
       (35, '02/01/2024', 'FRIEND', 28, 30),
       (36, '02/01/2024', 'FRIEND', 31, 28),
       (37, '02/01/2024', 'FRIEND', 28, 31),
       (38, '02/01/2024', 'FRIEND', 32, 28),
       (39, '02/01/2024', 'FRIEND', 28, 32),
       (40, '02/01/2024', 'FRIEND', 33, 28),
       (41, '02/01/2024', 'FRIEND', 28, 33),
       (42, '02/01/2024', 'FRIEND', 34, 28),
       (43, '02/01/2024', 'FRIEND', 28, 34),
       (44, '02/01/2024', 'FRIEND', 35, 28),
       (45, '02/01/2024', 'FRIEND', 28, 35),
       (46, '02/01/2024', 'FRIEND', 36, 28),
       (47, '02/01/2024', 'FRIEND', 28, 36),
       (48, '02/01/2024', 'FRIEND', 37, 28),
       (49, '02/01/2024', 'FRIEND', 28, 37),
       (50, '02/01/2024', 'FRIEND', 38, 28),
       (51, '02/01/2024', 'FRIEND', 28, 38)
ON CONFLICT (id) DO UPDATE
    SET sent_time     = EXCLUDED.sent_time,
        status_name   = EXCLUDED.status_name,
        dst_person_id = EXCLUDED.dst_person_id,
        src_person_id = EXCLUDED.src_person_id;
