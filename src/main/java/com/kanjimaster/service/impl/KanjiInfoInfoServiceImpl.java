package com.kanjimaster.service.impl;

import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.repository.KanjiInfoRepository;
import com.kanjimaster.service.KanjiInfoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class KanjiInfoInfoServiceImpl implements KanjiInfoService {
    private final KanjiInfoRepository kanjiInfoRepository;

    public KanjiInfoInfoServiceImpl(KanjiInfoRepository kanjiInfoRepository) {
        this.kanjiInfoRepository = kanjiInfoRepository;
    }
    @Override
    public KanjiInfo save(KanjiInfo kanji) {
        return kanjiInfoRepository.save(kanji);
    }

    @Override
    public KanjiInfo getKanji(String kanji) {
        return kanjiInfoRepository.getReferenceById(kanji);
    }

    @Override
    public Page<KanjiInfo> getKanjiPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return kanjiInfoRepository.findAll(pageable);
    }

    @Override
    public Collection<KanjiInfo> getKanjiesByRadical(Set<String> radicals) {
        return kanjiInfoRepository.findKanjiesByRadicals(radicals.toArray(new String[0]));
    }
}
