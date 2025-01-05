drop table if exists word cascade;
create table word
(
    id int8 generated always as identity,
    word text not null,
    reading text,
    translations text,
    primary key(id)
);
