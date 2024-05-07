create table reading
(
    id      bigserial not null,
    reading varchar(255) unique,
    primary key (id)
);
create table reading_main_kanji
(
    reading_id bigint not null,
    main_kanji varchar(255)
);
create table reading_name_kanji
(
    reading_id bigint not null,
    name_kanji varchar(255)
);
create table kanji_kun_reading
(
    kanji_id   bigint not null,
    reading_id bigint not null,
    primary key (kanji_id, reading_id)
);
create table kanji_name_reading
(
    kanji_id   bigint not null,
    reading_id bigint not null,
    primary key (kanji_id, reading_id)
);
create table kanji_on_reading
(
    kanji_id   bigint not null,
    reading_id bigint not null,
    primary key (kanji_id, reading_id)
);

alter table if exists kanji_kun_reading
    add constraint fk_reading_kanji_kun_reading foreign key (kanji_id) references reading;
alter table if exists kanji_kun_reading
    add constraint fk_kanji_kanji_kun_reading foreign key (reading_id) references kanji;
alter table if exists kanji_name_reading
    add constraint fk_reading_kanji_name_reading foreign key (kanji_id) references reading;
alter table if exists kanji_name_reading
    add constraint fk_kanji_kanji_name_reading foreign key (reading_id) references kanji;
alter table if exists kanji_on_reading
    add constraint fk_reading_kanji_on_reading foreign key (kanji_id) references reading;
alter table if exists kanji_on_reading
    add constraint fk_kanji_kanji_on_reading foreign key (reading_id) references kanji;