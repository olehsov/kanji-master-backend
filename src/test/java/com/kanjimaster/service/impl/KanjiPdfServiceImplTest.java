package com.kanjimaster.service.impl;

import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.support.TestData;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class KanjiPdfServiceImplTest {
    private final KanjiPdfServiceImpl service = new KanjiPdfServiceImpl();

    @Test
    void buildsPdfForKanjiList() throws Exception {
        Map<KanjiInfo, String> kanji = new LinkedHashMap<>();
        kanji.put(TestData.kanji("日", 1), "日本(nihon)");
        kanji.put(TestData.kanji("月", 2), "");

        byte[] pdf = service.buildKanjiLearnFile(kanji);

        assertThat(new String(pdf, 0, 5, StandardCharsets.US_ASCII)).isEqualTo("%PDF-");
        assertThat(pdf.length).isGreaterThan(1000);
    }

    @Test
    void toleratesMissingFields() throws Exception {
        KanjiInfo bare = KanjiInfo.builder().kanji("鬱").build();
        Map<KanjiInfo, String> kanji = new LinkedHashMap<>();
        kanji.put(bare, null);
        kanji.put(TestData.kanji("日", 1), "日本(nihon)");

        assertThat(service.buildKanjiLearnFile(kanji)).isNotEmpty();
    }
}
