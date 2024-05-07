package com.kanjimaster.service;

import com.kanjimaster.filter.kanji.KanjiFilter;
import com.kanjimaster.model.Kanji;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface KanjiService {
    Iterable<Kanji> saveAll(Collection<Kanji> kanjis);
    Kanji save(Kanji kanji);
    boolean isTableEmpty();
    List<Kanji> getKanjis();
    Kanji getKanji(Long id);
    Page<Kanji> getKanjiPage(int page, int size, KanjiFilter filter) throws Exception;
}
