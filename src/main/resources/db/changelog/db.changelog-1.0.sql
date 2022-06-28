--liquibase formatted sql

--changeset kaliuzhnyi:1
create table if not exists users
(
    id                 bigserial primary key,
    name               varchar(50),
    email              varchar(50)  not null unique,
    password           varchar(128) not null,
    created_date       timestamp,
    last_modified_date timestamp
);
alter sequence users_id_seq restart with 100000;

--changeset kaliuzhnyi:2
create table if not exists rooms
(
    id                 bigserial primary key,
    title              varchar(100) not null,
    description        varchar(1000),
    password           varchar(128),
    owner_id           bigint       not null references users,
    created_date       timestamp,
    last_modified_date timestamp
);
alter sequence rooms_id_seq restart with 100000;

--changeset kaliuzhnyi:3
create table if not exists guests
(
    id   bigserial primary key,
    name varchar(50) not null,
    type varchar(50) not null
);
alter sequence users_id_seq restart with 1;

--changeset kaliuzhnyi:4
create table if not exists votes
(
    id                 bigserial primary key,
    room_id            bigint not null references rooms,
    guest_id           bigint not null references guests,
    value              varchar(25),
    comment            varchar(100),
    created_date       timestamp,
    last_modified_date timestamp
);

--changeset kaliuzhnyi:5
create table if not exists guests_users
(
    guest_id bigint not null unique references guests,
    user_id  bigint references users,
    unique (guest_id, user_id)
);

--changeset kaliuzhnyi:6
create table if not exists guests_rooms
(
    id            bigserial primary key,
    guest_id      bigint    not null references guests,
    room_id       bigint references rooms,
    access_date   timestamp not null,
    access_status boolean,
    unique (guest_id, room_id)
);

--changeset kaliuzhnyi:7
create table if not exists users_remind_password
(
    user_id      bigint      not null references users (id),
    uuid         varchar(50) not null,
    created_date timestamp   not null,
    status       bool        not null default false,
    unique (user_id, uuid)
);