package com.kanjimaster.service;

import com.kanjimaster.model.updated.Word;

public interface WordService {
    Word createWord(Word word);
    boolean isEmpty();
}
