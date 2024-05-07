package com.kanjimaster.service.impl;

import com.kanjimaster.filter.builders.KanjiSpecificationsBuilder;
import com.kanjimaster.filter.kanji.KanjiFilter;
import com.kanjimaster.filter.specifications.KanjiSpecification;
import com.kanjimaster.model.Kanji;
import com.kanjimaster.repository.KanjiRepository;
import com.kanjimaster.service.KanjiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public Kanji save(Kanji kanji) {
        return kanjiRepository.save(kanji);
    }

    @Override
    public boolean isTableEmpty() {
        return kanjiRepository.count() == 0;
    }

    @Override
    public List<Kanji> getKanjis() {
        return kanjiRepository.findAll();
    }

    @Override
    public Kanji getKanji(Long id) {
        return kanjiRepository.getReferenceById(id);
    }

    @Override
    public Page<Kanji> getKanjiPage(int page, int size, KanjiFilter filter) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        if (Objects.isNull(filter)) {
            return kanjiRepository.findAll(pageable);
        }
        final Specification<Kanji> specification = new KanjiSpecificationsBuilder(
                filter.getJlptSafe(),
                filter.getGradeSafe(),
                filter.getStrokeCountSafe()
        ).build();
        return kanjiRepository.findAll(specification, pageable);
    }
}
