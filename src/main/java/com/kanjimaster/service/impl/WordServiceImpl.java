package com.kanjimaster.service.impl;

import com.kanjimaster.model.updated.Word;
import com.kanjimaster.repository.WordRepository;
import com.kanjimaster.service.WordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;
    public WordServiceImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public boolean isEmpty() {
        return wordRepository.count() == 0;
    }

    @Override
    public List<Word> save(List<Word> words) {
        return wordRepository.saveAll(words);
    }

    @Override
    public Page<Word> getWordPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wordRepository.findAll(pageable);
    }
}
