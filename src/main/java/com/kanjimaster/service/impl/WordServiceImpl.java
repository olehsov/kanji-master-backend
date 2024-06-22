package com.kanjimaster.service.impl;

import com.kanjimaster.model.Word;
import com.kanjimaster.repository.WordRepository;
import com.kanjimaster.service.WordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;

    public WordServiceImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public Page<Word> getWordByKanjiPage(int page, int size, Long kanjiId) {
        Pageable pageable = PageRequest.of(page, size);
        return wordRepository.findAllByKanji_Id(kanjiId, pageable);
    }

    @Override
    public Page<Word> getWordPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wordRepository.findAll(pageable);
    }
}
