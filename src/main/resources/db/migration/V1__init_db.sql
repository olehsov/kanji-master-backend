drop table if exists users cascade;
create table users (
    id int8 generated always as identity,
    created_at timestamp(6),
    email varchar(255) unique,
    username varchar(255),
    is_active boolean default false,
    password varchar(255),
    primary key (id)
);