package com.kanjimaster.service.impl;

import com.kanjimaster.model.updated.Word;
import com.kanjimaster.repository.WordRepository;
import com.kanjimaster.service.WordService;
import org.springframework.stereotype.Service;

@Service
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;
    public WordServiceImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public Word createWord(Word word) {
        return wordRepository.save(word);
    }

    @Override
    public boolean isEmpty() {
        return wordRepository.count() == 0;
    }
}
