create table word
(
    id       bigserial not null,
    kanji_id bigint,
    primary key (id)
);
create table variant
(
    id         bigserial not null,
    pronounced varchar(255),
    written    varchar(255),
    primary key (id)
);
create table variant_priorities
(
    variant_id bigint not null,
    priorities varchar(255)
);
create table word_variant
(
    variant_id bigint not null,
    word_id    bigint not null,
    primary key (variant_id, word_id)
);
create table meaning
(
    id bigserial not null,
    primary key (id)
);
create table word_meaning
(
    meaning_id bigint not null,
    word_id    bigint not null,
    primary key (meaning_id, word_id)
);
create table kanji_meanings
(
    kanji_id bigint not null,
    meanings varchar(255)
);
create table meaning_glosses
(
    meaning_id bigint not null,
    glosses    varchar(2048)
);
alter table if exists kanji_meanings
    add constraint fk_kanji_meanings foreign key (kanji_id) references kanji;
alter table if exists meaning_glosses
    add constraint fk_meaning_glosses foreign key (meaning_id) references meaning;
alter table if exists reading_main_kanji
    add constraint fk_reading_main_kanji foreign key (reading_id) references reading;
alter table if exists reading_name_kanji
    add constraint fk_reading_name_kanji foreign key (reading_id) references reading;
alter table if exists variant_priorities
    add constraint fk_variant_priorities foreign key (variant_id) references variant;
alter table if exists word
    add constraint fk_word foreign key (kanji_id) references kanji;
alter table if exists word_meaning
    add constraint fk_meaning_word_meaning foreign key (meaning_id) references meaning;
alter table if exists word_meaning
    add constraint fk_word_word_meaning foreign key (word_id) references word;
alter table if exists word_variant
    add constraint fk_variant_word_variant foreign key (variant_id) references variant;
alter table if exists word_variant
    add constraint fk_word_word_variant foreign key (word_id) references word;