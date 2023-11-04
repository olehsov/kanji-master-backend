package com.kanjimaster.service.impl;

import com.kanjimaster.model.Kanji;
import com.kanjimaster.repository.KanjiRepository;
import com.kanjimaster.service.KanjiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class KanjiServiceImpl implements KanjiService {
    private final KanjiRepository kanjiRepository;

    public KanjiServiceImpl(KanjiRepository kanjiRepository) {
        this.kanjiRepository = kanjiRepository;
    }

    @Override
    public Iterable<Kanji> saveAll(Collection<Kanji> kanjis) {
        return kanjiRepository.saveAll(kanjis);
    }

    @Override
    public boolean isKanjiTableEmpty() {
        return kanjiRepository.count() == 0;
    }

    @Override
    public List<Kanji> getKanjies() {
        return kanjiRepository.findAll();
    }

    @Override
    public Kanji getKanji(Long id) {
        return kanjiRepository.getReferenceById(id);
    }

    @Override
    public Page<Kanji> getKanjiPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return kanjiRepository.findAll(pageable);
    }
}
