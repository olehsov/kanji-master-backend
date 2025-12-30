DROP TABLE IF EXISTS INDIVIDUAL_SUBTITLE_TRANSLATION CASCADE ;
DROP TABLE IF EXISTS VIDEO_SUBTITLE CASCADE ;
DROP TABLE IF EXISTS VIDEO_METADATA CASCADE ;

CREATE TABLE VIDEO_METADATA (
    id int8 generated always as identity,
    title VARCHAR(255),
    youtube_id VARCHAR(255),
    primary key (id)
);

CREATE TABLE VIDEO_SUBTITLE (
    id int8 generated always as identity,
    index INT NOT NULL,
    timing_start VARCHAR(32),
    timing_end VARCHAR(32),
    line TEXT,
    translation TEXT,
    kanjies VARCHAR(255),
    grammars VARCHAR(255),
    video_id int8 not null references VIDEO_METADATA(id),
    primary key (id)
);

CREATE TABLE INDIVIDUAL_SUBTITLE_TRANSLATION (
    id int8 generated always as identity,
    text VARCHAR(255) NOT NULL,
    text_hiragana VARCHAR(255),
    meaning TEXT,
    is_word BOOLEAN NOT NULL,
    jlpt VARCHAR(10),
    subtitle_id int8 not null references VIDEO_SUBTITLE(id),
    primary key (id)
);

