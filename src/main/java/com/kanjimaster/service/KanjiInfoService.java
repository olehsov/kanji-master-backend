package com.kanjimaster.service;

import com.kanjimaster.dto.KanjiFilterDdo;
import com.kanjimaster.dto.KanjiInfoDto;
import com.kanjimaster.model.updated.KanjiInfo;
import org.springframework.data.domain.Page;

import java.util.*;

public interface KanjiInfoService {
    KanjiInfo save(KanjiInfo kanji);
    List<KanjiInfo> save(List<KanjiInfo> kanji);
    KanjiInfoDto getKanji(String kanji);
    Page<KanjiInfo> getKanjiPage(int page, int size, KanjiFilterDdo filter) throws Exception;
    Map<KanjiInfo, String> getKanjiesByFilterWithWordRep(KanjiFilterDdo filter) throws Exception;
    Collection<KanjiInfo> getKanjiesByRadical(Set<String> radicals);
}
