drop table if exists word cascade;
create table word
(
    id int8 generated always as identity,
    word TEXT NOT NULL,
    yomi_display TEXT,
    romaji_display TEXT,
    freq INTEGER,
    is_self_supporting BOOLEAN,
    translation TEXT,
    primary key(id)
);