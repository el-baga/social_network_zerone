SELECT setval('persons_id_seq', 100, true);
SELECT setval('person_settings_id_seq', 100, true);
SELECT setval('posts_id_seq', 100, true);

insert into person_settings (id, comment_comment, friend_birthday, friend_request, message, post, post_comment, post_like)
values (1, true, true, true, true, true, true, true),
       (2, true, true, true, true, true, true, true),
       (3, true, true, true, true, true, true, true),
       (4, true, true, true, true, true, true, true),
       (5, true, true, true, true, true, true, true);

insert into persons (id, first_name, last_name, e_mail, message_permissions, online_status, person_settings_id, about, city, country, birth_date, is_blocked, last_online_time, reg_date, is_deleted, phone, photo, password)
values (1, 'Baga', 'Java', 'baga@mail.com', 'ALL', true, 1, 'about1', 'Москва', 'Россия', '2003-01-01 00:00:00', false, '2024-02-09 15:10:00.000000', null, false, '79991123322', 'lizzard-ava.jpg', 'javaSE2024'),
       (2, 'Bob', 'Martin', 'alex@top.ru', 'ALL', false, 2, 'about2', 'Санкт-Петербург', 'Россия', '2002-10-22 00:00:00', false, '2024-02-09 15:10:00.000000', null, false, '79311659000', 'python-test.png', 'debug7'),
       (3, 'Yman', 'Back', 'ymanchik@back.ru', 'ALL', false, 3, 'about3', 'Сочи', 'Россия', '1990-11-11 00:00:00', false, '2024-02-09 15:10:00.000000', null, false, '78333124779', 'javaCrush.jpeg', 'backend45'),
       (4, 'Alex', 'Martin', 'counter@alex.ru', 'ALL', false, 4, 'about3', 'Сочи', 'Россия', '1995-11-11 00:00:00', false, '2024-02-19 15:10:00.000000', null, true, '70000000000', 'javaCrush23.jpeg', 'happyDays2222'),
       (5, 'Irina', 'Shaeck', 'test-block@mail.ru', 'ALL', false, 5, 'about3', 'Сочи', 'Россия', '1995-11-11 00:00:00', true, '2024-02-19 15:10:00.000000', null, true, '70000089899', 'irina.jpeg', 'irina2023');

insert into posts (id, title, post_text, time, is_blocked, is_deleted, time_delete, author_id)
values (1, 'Hello', '<p>Всем привет. Подключился к этому замечательному сервису. <strong>Будем общаться!</strong></p>', '2024-02-09 15:00:00.000000', false, false, null, 1),
       (2, 'Happy holidays', '<p>Поздравляю всех с новым годом. <strong>Будьте здоровы!</strong></p>', '2024-01-19 18:10:01.000000', false, false, null, 1);
