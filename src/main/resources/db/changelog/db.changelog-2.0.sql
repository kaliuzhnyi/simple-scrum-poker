--liquibase formatted sql

--changeset kaliuzhnyi:1
create table retros
(
    id                 bigserial primary key,
    title              varchar(50)  not null,
    description        varchar(1000),
    status             varchar(50),
    password           varchar(100) not null,
    owner_id           bigint       not null references users(id),
    created_date       timestamp,
    last_modified_date timestamp
);
alter sequence retros_id_seq restart with 100000;