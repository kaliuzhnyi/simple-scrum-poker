--liquibase formatted sql

--changeset kaliuzhnyi:1
create table if not exists retros
(
    id                 bigserial primary key,
    title              varchar(50)  not null,
    description        varchar(1000),
    status             varchar(50)  not null,
    password           varchar(100) not null,
    owner_id           bigint       not null references users (id),
    created_date       timestamp,
    last_modified_date timestamp
);
alter sequence retros_id_seq restart with 100000;

--changeset kaliuzhnyi:2
create table if not exists guests_retros
(
    id            bigserial primary key,
    guest_id      bigint    not null references guests (id),
    retro_id      bigint    not null references retros (id),
    access_date   timestamp not null,
    access_status boolean,
    unique (guest_id, retro_id)
);

--changeset kaliuzhnyi:3
create table if not exists retro_messages
(
    id                 bigserial primary key,
    retro_id           bigint       not null references retros (id),
    guest_id           bigint       not null references guests (id),
    value              varchar(250) not null,
    type               varchar(25)  not null,
    created_date       timestamp,
    last_modified_date timestamp
);

--changeset kaliuzhnyi:4
create table if not exists retro_message_likes
(
    id                 bigserial primary key,
    message_id         bigint not null references retro_messages (id),
    guest_id           bigint not null references guests (id),
    created_date       timestamp,
    last_modified_date timestamp
);