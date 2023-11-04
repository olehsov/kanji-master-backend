drop table if exists kanji cascade;
create table kanji (
    grade integer default null,
    jlpt integer default null,
    stroke_count integer,
    id bigserial not null,
    heisig_en varchar(255) default null,
    kanji varchar(255),
    unicode varchar(255),
    primary key (id)
);
create table kanji_kun_readings (kanji_id bigint not null, kun_readings varchar(255));
create table kanji_meanings (kanji_id bigint not null, meanings varchar(255));
create table kanji_name_readings (kanji_id bigint not null, name_readings varchar(255));
create table kanji_notes (kanji_id bigint not null, notes varchar(255));
create table kanji_on_readings (kanji_id bigint not null, on_readings varchar(255));
alter table if exists kanji_kun_readings add constraint FKa4cdm54pm5ywm3t4tkohhgumu foreign key (kanji_id) references kanji;
alter table if exists kanji_meanings add constraint FK1aqpq36abww9un3loogmibnrq foreign key (kanji_id) references kanji;
alter table if exists kanji_name_readings add constraint FK9rofu3s94jpn56rutvfsouhb8 foreign key (kanji_id) references kanji;
alter table if exists kanji_notes add constraint FK4u44vr8m6644bv2as3suht1lb foreign key (kanji_id) references kanji;
alter table if exists kanji_on_readings add constraint FKamxof4oddk9mhdxve90vj4yd9 foreign key (kanji_id) references kanji;