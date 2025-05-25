package com.kanjimaster.service;


import com.kanjimaster.model.updated.Word;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WordService {
    boolean isEmpty();
    List<Word> save(List<Word> words);
    Page<Word> getWordPage(int page, int size) throws Exception;

}
