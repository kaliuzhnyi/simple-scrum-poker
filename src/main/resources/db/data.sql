
set session var.users.password.def = '{bcrypt}$2a$10$u30HwVxL.nqbqkB8T23dUOVZu..IXXFEl9zG3kpGtGFT2j1Y6Or/K';
set session var.users.created_date.def = '2022-01-01 12:00:00';
set session var.users.last_modified_date.def = '2022-01-02 12:00:00';

set session var.users.user1.id = 100001;
set session var.users.user1.name = 'Ivan Kaliuzhnyi';
set session var.users.user1.email = 'kalyuzhny.ivan@gmail.com';

set session var.users.user2.id = 100002;
set session var.users.user2.name = 'Petr Petrov';
set session var.users.user2.email = 'petrov.petr@gmail.com';

set session var.users.user3.id = 100003;
set session var.users.user3.name = 'Ivan Ivanov';
set session var.users.user3.email = 'ivanov.ivan@gmail.com';

set session var.users.user4.id = 100004;
set session var.users.user4.name = 'Igor Fedorov';
set session var.users.user4.email = 'fedorov.igor@gmail.com';

set session var.users.user5.id = 100005;
set session var.users.user5.name = 'Anna Svetova';
set session var.users.user5.email = 'svetova.anna@gmail.com';

--

set session var.rooms.password.def = 'pass0126377';
set session var.rooms.created_date.def = '2022-01-01 12:00:00';
set session var.rooms.last_modified_date.def = '2022-01-02 12:00:00';

set session var.rooms.room1.id = 100001;
set session var.rooms.room1.name = 'Room #1';
set session var.rooms.room1.description = 'Description room #1';
set session var.rooms.room1.owner.id = 100001;

set session var.rooms.room2.id = 100002;
set session var.rooms.room2.name = 'Room #2';
set session var.rooms.room2.description = 'Description room #2';
set session var.rooms.room2.owner.id = 100001;

set session var.rooms.room3.id = 100003;
set session var.rooms.room3.name = 'Room #3';
set session var.rooms.room3.description = 'Description room #3';
set session var.rooms.room3.owner.id = 100001;

set session var.rooms.room4.id = 100004;
set session var.rooms.room4.name = 'Room #4';
set session var.rooms.room4.description = 'Description room #4';
set session var.rooms.room4.owner.id = 100001;

set session var.rooms.room5.id = 100005;
set session var.rooms.room5.name = 'Room #5';
set session var.rooms.room5.description = 'Description room #5';
set session var.rooms.room5.owner.id = 100001;


-- users (password - 0126377)
-- truncate votes;
-- truncate guests_rooms;
-- truncate rooms cascade;
truncate users cascade;
select setval(pg_get_serial_sequence('users', 'id'), 100001);
select currval(pg_get_serial_sequence('users', 'id'));
insert into users
values (current_setting('var.users.user1.id')::bigint, current_setting('var.users.user1.name'), current_setting('var.users.user1.email'), current_setting('var.users.password.def'), current_setting('var.users.created_date.def')::timestamp, current_setting('var.users.last_modified_date.def')::timestamp),
       (current_setting('var.users.user2.id')::bigint, current_setting('var.users.user2.name'), current_setting('var.users.user2.email'), current_setting('var.users.password.def'), current_setting('var.users.created_date.def')::timestamp, current_setting('var.users.last_modified_date.def')::timestamp),
       (current_setting('var.users.user3.id')::bigint, current_setting('var.users.user3.name'), current_setting('var.users.user3.email'), current_setting('var.users.password.def'), current_setting('var.users.created_date.def')::timestamp, current_setting('var.users.last_modified_date.def')::timestamp),
       (current_setting('var.users.user4.id')::bigint, current_setting('var.users.user4.name'), current_setting('var.users.user4.email'), current_setting('var.users.password.def'), current_setting('var.users.created_date.def')::timestamp, current_setting('var.users.last_modified_date.def')::timestamp),
       (current_setting('var.users.user5.id')::bigint, current_setting('var.users.user5.name'), current_setting('var.users.user5.email'), current_setting('var.users.password.def'), current_setting('var.users.created_date.def')::timestamp, current_setting('var.users.last_modified_date.def')::timestamp);
select setval(pg_get_serial_sequence('users', 'id'), coalesce(max(id)+1, 1)) from users;

-- room
truncate rooms cascade;
select setval(pg_get_serial_sequence('rooms', 'id'), 100001);
select currval(pg_get_serial_sequence('rooms', 'id'));
insert into rooms (id, title, description, password, owner_id, created_date, last_modified_date)
values (current_setting('var.rooms.room1.id')::bigint, current_setting('var.rooms.room1.name'), current_setting('var.rooms.room1.description'), current_setting('var.rooms.password.def'), current_setting('var.rooms.room1.owner.id')::int, current_setting('var.rooms.created_date.def')::timestamp, current_setting('var.rooms.last_modified_date.def')::timestamp),
       (current_setting('var.rooms.room2.id')::bigint, current_setting('var.rooms.room2.name'), current_setting('var.rooms.room2.description'), current_setting('var.rooms.password.def'), current_setting('var.rooms.room2.owner.id')::int, current_setting('var.rooms.created_date.def')::timestamp, current_setting('var.rooms.last_modified_date.def')::timestamp),
       (current_setting('var.rooms.room3.id')::bigint, current_setting('var.rooms.room3.name'), current_setting('var.rooms.room3.description'), current_setting('var.rooms.password.def'), current_setting('var.rooms.room3.owner.id')::int, current_setting('var.rooms.created_date.def')::timestamp, current_setting('var.rooms.last_modified_date.def')::timestamp),
       (current_setting('var.rooms.room4.id')::bigint, current_setting('var.rooms.room4.name'), current_setting('var.rooms.room4.description'), current_setting('var.rooms.password.def'), current_setting('var.rooms.room4.owner.id')::int, current_setting('var.rooms.created_date.def')::timestamp, current_setting('var.rooms.last_modified_date.def')::timestamp),
       (current_setting('var.rooms.room5.id')::bigint, current_setting('var.rooms.room5.name'), current_setting('var.rooms.room5.description'), current_setting('var.rooms.password.def'), current_setting('var.rooms.room5.owner.id')::int, current_setting('var.rooms.created_date.def')::timestamp, current_setting('var.rooms.last_modified_date.def')::timestamp);
select setval(pg_get_serial_sequence('rooms', 'id'), coalesce(max(id)+1, 1)) from rooms;

-- guests
truncate guests cascade;
insert into guests (name, type)
values (current_setting('var.users.user1.name'), 'USER'),
       (current_setting('var.users.user2.name'), 'USER'),
       (current_setting('var.users.user3.name'), 'USER'),
       (current_setting('var.users.user4.name'), 'USER'),
       (current_setting('var.users.user5.name'), 'USER'),
       ('Pablo Picasso', 'ANONYMOUS'),
       ('Without Vote', 'ANONYMOUS');

-- guests_users
truncate guests_users cascade;
insert into guests_users (guest_id, user_id)
values ((select id from guests where name = current_setting('var.users.user1.name')), current_setting('var.users.user1.id')::bigint),
       ((select id from guests where name = current_setting('var.users.user2.name')), current_setting('var.users.user2.id')::bigint),
       ((select id from guests where name = current_setting('var.users.user3.name')), current_setting('var.users.user3.id')::bigint),
       ((select id from guests where name = current_setting('var.users.user4.name')), current_setting('var.users.user4.id')::bigint),
       ((select id from guests where name = current_setting('var.users.user5.name')), current_setting('var.users.user5.id')::bigint);

-- guests_rooms
truncate guests_rooms cascade;
insert into guests_rooms (guest_id, room_id, access_date, access_status)
values ((select id from guests where name = current_setting('var.users.user1.name')), current_setting('var.rooms.room1.id')::bigint, current_setting('var.rooms.created_date.def')::timestamp, true),
       ((select id from guests where name = current_setting('var.users.user2.name')), current_setting('var.rooms.room1.id')::bigint, current_setting('var.rooms.created_date.def')::timestamp, true),
       ((select id from guests where name = current_setting('var.users.user3.name')), current_setting('var.rooms.room1.id')::bigint, current_setting('var.rooms.created_date.def')::timestamp, true),
       ((select id from guests where name = current_setting('var.users.user4.name')), current_setting('var.rooms.room1.id')::bigint, current_setting('var.rooms.created_date.def')::timestamp, true),
       ((select id from guests where name = current_setting('var.users.user5.name')), current_setting('var.rooms.room1.id')::bigint, current_setting('var.rooms.created_date.def')::timestamp, true),
       ((select id from guests where name = 'Pablo Picasso'), current_setting('var.rooms.room1.id')::bigint, current_setting('var.rooms.created_date.def')::timestamp, true),
       ((select id from guests where name = 'Without Vote'), current_setting('var.rooms.room1.id')::bigint, current_setting('var.rooms.created_date.def')::timestamp, true);

-- votes
truncate votes cascade;
insert into votes (room_id, guest_id, value, comment, created_date, last_modified_date)
values (current_setting('var.rooms.room1.id')::bigint, (select id from guests where name = 'Ivan Kaliuzhnyi'), 'VALUE_0', 'It is so easy', '2022-05-26 23:01:00.436693', '2022-05-26 23:01:00.436693'),
       (current_setting('var.rooms.room1.id')::bigint, (select id from guests where name = 'Petr Petrov'), 'VALUE_13', 'It is so hard for me.', '2022-05-26 23:02:03.717598', '2022-05-26 23:02:03.717598'),
       (current_setting('var.rooms.room1.id')::bigint, (select id from guests where name = 'Ivan Ivanov'), 'VALUE_UNKNOWN', 'Actually I do not understand task.', '2022-05-26 23:03:05.607584', '2022-05-26 23:03:05.607584'),
       (current_setting('var.rooms.room1.id')::bigint, (select id from guests where name = 'Igor Fedorov'), 'VALUE_5', 'No problem', '2022-05-26 23:04:07.308211', '2022-05-26 23:04:07.308211'),
       (current_setting('var.rooms.room1.id')::bigint, (select id from guests where name = 'Anna Svetova'), 'VALUE_100', '', '2022-05-26 23:05:09.821264', '2022-05-26 23:05:09.821264'),
       (current_setting('var.rooms.room1.id')::bigint, (select id from guests where name = 'Pablo Picasso'), 'VALUE_5', '', '2022-05-26 23:06:09.821264', '2022-05-26 23:06:09.821264'),
       (current_setting('var.rooms.room2.id')::bigint, (select id from guests where name = 'Ivan Kaliuzhnyi'), 'VALUE_0', 'It is so easy', '2022-05-26 23:07:00.436693', '2022-05-26 23:07:00.436693'),
       (current_setting('var.rooms.room2.id')::bigint, (select id from guests where name = 'Petr Petrov'), 'VALUE_13', 'It is so hard for me.', '2022-05-26 23:08:03.717598', '2022-05-26 23:08:03.717598');

-- users_remind_password
truncate users_remind_password cascade;
insert into users_remind_password (user_id, uuid, created_date, status)
values (current_setting('var.users.user1.id')::bigint, 'ca06a3f9-043f-47f2-9478-3749ac324d66', now(), true),
       (current_setting('var.users.user1.id')::bigint, 'a698fae4-3296-4682-b20e-60dee4f81c5d', now(), false);