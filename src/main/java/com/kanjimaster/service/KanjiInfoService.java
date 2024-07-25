package com.kanjimaster.service;

import com.kanjimaster.dto.KanjiFilterDdo;
import com.kanjimaster.model.updated.KanjiInfo;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Set;

public interface KanjiInfoService {
    KanjiInfo save(KanjiInfo kanji);
    KanjiInfo getKanji(String kanji);
    Page<KanjiInfo> getKanjiPage(int page, int size, KanjiFilterDdo filter) throws Exception;
    Collection<KanjiInfo> getKanjiesByRadical(Set<String> radicals);
}
