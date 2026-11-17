package com.kanjimaster.repository;

import com.kanjimaster.model.IndividualSubtitleTranslation;
import com.kanjimaster.model.VideoMetadata;
import com.kanjimaster.model.VideoSubtitle;
import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.model.updated.Sentence;
import com.kanjimaster.support.PostgresContainer;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresContainer.class)
@Testcontainers(disabledWithoutDocker = true)
class RepositoryIT {
    @Autowired
    private KanjiInfoRepository kanjiInfoRepository;
    @Autowired
    private SearchRepository searchRepository;
    @Autowired
    private SentenceRepository sentenceRepository;
    @Autowired
    private RadicalRepository radicalRepository;
    @Autowired
    private RadVarRepository radVarRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private VideoMetadataRepository videoMetadataRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void migrationsLoadDictionaryData() {
        assertThat(kanjiInfoRepository.count()).isGreaterThan(2000);
        assertThat(radicalRepository.count()).isGreaterThan(100);
        assertThat(radVarRepository.count()).isPositive();
        assertThat(topicRepository.count()).isPositive();
        assertThat(kanjiInfoRepository.findById("日")).isPresent();
    }

    @Test
    void searchFindsKanjiByReading() {
        List<String> kanji = searchRepository.findAllKanjiBySearch("nichi");
        assertThat(kanji).contains("日");
        assertThat(searchRepository.findAllKanjiBySearch("zzzzzz")).isEmpty();
    }

    @Test
    void findKanjiesByRadicalsRequiresAllRadicals() {
        KanjiInfo sun = kanjiInfoRepository.findById("日").orElseThrow();
        String firstElement = sun.getElements().substring(0, 1);

        Collection<KanjiInfo> result = kanjiInfoRepository.findKanjiesByRadicals(new String[]{firstElement});

        assertThat(result).isNotEmpty().allSatisfy(k -> assertThat(k.getElements()).contains(firstElement));
        assertThat(kanjiInfoRepository.findKanjiesByRadicals(new String[]{firstElement, "☃"})).isEmpty();
    }

    @Test
    void wordRepresentationsUseTopThreeWordsExcludingTheKanjiItself() {
        insertWord(1001, "日本", "nihon", 100);
        insertWord(1002, "毎日", "mainichi", 90);
        insertWord(1003, "日曜日", "nichiyoubi", 80);
        insertWord(1004, "日記", "nikki", 10);
        insertWord(1005, "日", "hi", 1000);

        List<Object[]> rows = kanjiInfoRepository.findKanjiWordRepresentations(List.of("日", "鬱"));

        assertThat(rows).singleElement().satisfies(row -> {
            assertThat(row[0]).isEqualTo("日");
            assertThat((String) row[1]).contains("日本(nihon)", "毎日(mainichi)", "日曜日(nichiyoubi)")
                    .doesNotContain("nikki", "(hi)");
        });
    }

    @Test
    void sentencesContainingKanjiArePaged() {
        Page<Sentence> page = sentenceRepository.findAllByWordContains(PageRequest.of(0, 5), "日");
        assertThat(page.getContent()).isNotEmpty().hasSizeLessThanOrEqualTo(5)
                .allSatisfy(s -> assertThat(s.getWord()).contains("日"));
    }

    @Test
    void videoSubtitlesCascadeAndYoutubeIdIsUnique() {
        videoMetadataRepository.saveAndFlush(video("qal34e9v_pk"));
        entityManager.clear();

        VideoMetadata loaded = videoMetadataRepository.findByYoutubeId("qal34e9v_pk").orElseThrow();
        assertThat(loaded.getVideoSubtitles()).singleElement()
                .satisfies(s -> assertThat(s.getIndividualTranslations()).hasSize(2));

        assertThatThrownBy(() -> videoMetadataRepository.saveAndFlush(video("qal34e9v_pk")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private void insertWord(long id, String word, String romaji, int freq) {
        entityManager.createNativeQuery("""
                        INSERT INTO word (id, word, yomi_display, romaji_display, freq, is_self_supporting, translation)
                        OVERRIDING SYSTEM VALUE VALUES (?, ?, '', ?, ?, true, '')""")
                .setParameter(1, id).setParameter(2, word).setParameter(3, romaji).setParameter(4, freq)
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO kanji_word (kanji, word_id) VALUES ('日', ?)")
                .setParameter(1, id).executeUpdate();
    }

    private static VideoMetadata video(String youtubeId) {
        VideoMetadata video = new VideoMetadata();
        video.setYoutubeId(youtubeId);
        video.setTitle("");
        VideoSubtitle subtitle = new VideoSubtitle();
        subtitle.setIndex(1);
        subtitle.setLine("猫と犬");
        subtitle.setVideoMetadata(video);
        for (String text : List.of("猫", "犬")) {
            IndividualSubtitleTranslation t = new IndividualSubtitleTranslation();
            t.setText(text);
            t.setWord(true);
            t.setVideoSubtitle(subtitle);
            subtitle.getIndividualTranslations().add(t);
        }
        video.getVideoSubtitles().add(subtitle);
        return video;
    }
}
