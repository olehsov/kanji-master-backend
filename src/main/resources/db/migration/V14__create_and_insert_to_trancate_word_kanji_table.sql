DROP TABLE IF EXISTS KANJI_WORD;

CREATE TABLE KANJI_WORD (
    kanji text not null references KANJI_INFO(kanji),
    word_id bigint not null references WORD(id),
    primary key (kanji, word_id)
);

INSERT INTO KANJI_WORD (kanji, word_id)
SELECT DISTINCT k.kanji, w.id
FROM WORD w, LATERAL regexp_split_to_table(w.word, '') AS c(kanji) JOIN KANJI_INFO k ON k.kanji = c.kanji;