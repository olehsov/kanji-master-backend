create table kanji
(
    grade        integer   not null,
    jlpt         integer   not null,
    stroke_count integer,
    id           bigserial not null,
    heisig_en    varchar(255),
    kanji        varchar(255),
    unicode      varchar(255),
    primary key (id)
);
create table kanji_notes
(
    kanji_id bigint not null,
    notes    varchar(255)
);
alter table if exists kanji_notes
    add constraint fk_kanji_notes foreign key (kanji_id) references kanji;

