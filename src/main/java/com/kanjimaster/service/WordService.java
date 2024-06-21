package com.kanjimaster.service;

import com.kanjimaster.model.Word;
import org.springframework.data.domain.Page;

public interface WordService {
    Page<Word> getWordByKanjiPage(int page, int size, Long kanjiId);
}
