package com.kanjimaster.service.impl;

import com.kanjimaster.model.updated.Sentence;
import com.kanjimaster.repository.SentenceRepository;
import com.kanjimaster.service.SentenceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SentenceServiceImpl implements SentenceService {
    private final SentenceRepository sentenceRepository;

    public SentenceServiceImpl(SentenceRepository sentenceRepository) {
        this.sentenceRepository = sentenceRepository;
    }

    @Override
    public Page<Sentence> findSentencesByKanji(int page, int size, String kanji) {
        Pageable pageable = PageRequest.of(page, size);
        return sentenceRepository.findAllByWordContains(pageable, kanji);
    }
}
