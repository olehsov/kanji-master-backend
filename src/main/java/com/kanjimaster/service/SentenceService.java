package com.kanjimaster.service;

import com.kanjimaster.model.updated.Sentence;
import org.springframework.data.domain.Page;

public interface SentenceService {
    Page<Sentence> findSentencesByKanji(int page, int size, String kanji);
}
