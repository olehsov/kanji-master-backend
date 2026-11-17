package com.kanjimaster.service.impl;

import com.kanjimaster.exception.BadRequestException;
import com.kanjimaster.model.updated.Sentence;
import com.kanjimaster.repository.SentenceRepository;
import com.kanjimaster.service.Paging;
import com.kanjimaster.service.SentenceService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SentenceServiceImpl implements SentenceService {
    private final SentenceRepository sentenceRepository;

    public SentenceServiceImpl(SentenceRepository sentenceRepository) {
        this.sentenceRepository = sentenceRepository;
    }

    @Override
    public Page<Sentence> findSentencesByKanji(int page, int size, String kanji) {
        if (kanji == null || kanji.isBlank()) {
            throw new BadRequestException("Kanji must not be empty");
        }
        return sentenceRepository.findAllByWordContains(Paging.of(page, size), kanji);
    }
}
