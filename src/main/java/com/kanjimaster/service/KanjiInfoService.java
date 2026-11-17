package com.kanjimaster.service;

import com.kanjimaster.dto.KanjiFilterDto;
import com.kanjimaster.dto.KanjiInfoDto;
import com.kanjimaster.model.updated.KanjiInfo;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface KanjiInfoService {
    KanjiInfoDto getKanji(String kanji);

    Page<KanjiInfo> getKanjiPage(int page, int size, KanjiFilterDto filter);

    /** Kanji matching the filter mapped to "word(romaji),..." of their 3 most frequent words ("" if none). */
    Map<KanjiInfo, String> getKanjiesByFilterWithWordRep(KanjiFilterDto filter);

    Collection<KanjiInfo> getKanjiesByRadical(Set<String> radicals);
}
