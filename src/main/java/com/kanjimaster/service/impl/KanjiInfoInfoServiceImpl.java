package com.kanjimaster.service.impl;

import com.kanjimaster.dto.KanjiFilterDdo;
import com.kanjimaster.filter.builders.KanjiInfoSpecificationsBuilder;
import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.repository.KanjiInfoRepository;
import com.kanjimaster.repository.SearchRepository;
import com.kanjimaster.service.KanjiInfoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KanjiInfoInfoServiceImpl implements KanjiInfoService {
    private final KanjiInfoRepository kanjiInfoRepository;
    private final SearchRepository searchRepository;

    public KanjiInfoInfoServiceImpl(KanjiInfoRepository kanjiInfoRepository, SearchRepository searchRepository) {
        this.kanjiInfoRepository = kanjiInfoRepository;
        this.searchRepository = searchRepository;
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
    public Page<KanjiInfo> getKanjiPage(int page, int size, KanjiFilterDdo filter) throws Exception {
        Pageable pageable = PageRequest.of(page, size);

        if (Objects.isNull(filter))
            return kanjiInfoRepository.findAll(pageable);
        List<String> kanjiList = Collections.emptyList();
        if (Objects.nonNull(filter.getSearch()))
            kanjiList = searchRepository.findAllKanjiBySearch(filter.getSearch());

        Specification<KanjiInfo> specification = new KanjiInfoSpecificationsBuilder(
                Optional.ofNullable(filter.getGrade()).orElse(Collections.emptyList()),
                Optional.ofNullable(filter.getJlpt()).orElse(Collections.emptyList()),
                kanjiList
        ).build();
        return kanjiInfoRepository.findAll(specification, pageable);
    }

    @Override
    public Collection<KanjiInfo> getKanjiesByRadical(Set<String> radicals) {
        return kanjiInfoRepository.findKanjiesByRadicals(radicals.toArray(new String[0]));
    }
}
