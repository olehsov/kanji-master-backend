package com.kanjimaster.service;

import com.kanjimaster.model.Kanji;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface KanjiService {
    Iterable<Kanji> saveAll(Collection<Kanji> kanjis);
    boolean isKanjiTableEmpty();
    List<Kanji> getKanjies();
    Kanji getKanji(Long id);
    Page<Kanji> getKanjiPage(int page, int size);
}
