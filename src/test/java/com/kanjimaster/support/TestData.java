package com.kanjimaster.support;

import com.kanjimaster.model.Topic;
import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.model.updated.Word;

import java.util.List;

/** Small builders for test entities. */
public final class TestData {
    private TestData() {
    }

    public static KanjiInfo kanji(String kanji, Integer frequency) {
        return KanjiInfo.builder()
                .kanji(kanji)
                .frequency(frequency)
                .meaning("day;sun")
                .onyomi("ニチ")
                .kunyomi("ひ")
                .grade("1")
                .jlpt("5")
                .words(List.of())
                .build();
    }

    public static Word word(long id, String text, int freq) {
        return Word.builder().id(id).word(text).yomiDisplay("にほん").romajiDisplay("nihon").freq(freq)
                .translation("Японія").build();
    }

    public static Topic topic(long id, String header) {
        Topic topic = new Topic();
        topic.setId(id);
        topic.setHeader(header);
        topic.setDescription("description of " + header);
        return topic;
    }
}
