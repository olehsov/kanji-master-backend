package com.kanjimaster.service.impl;

import com.kanjimaster.dto.KanjiFilterDdo;
import com.kanjimaster.dto.KanjiInfoDto;
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

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public List<KanjiInfo> save(List<KanjiInfo> kanji) {
        return kanjiInfoRepository.saveAll(kanji);
    }

    @Override
    public KanjiInfoDto getKanji(String kanji) {
        return KanjiInfoDto.fromKanjiInfo(kanjiInfoRepository.getReferenceById(kanji));
    }

    @Override
    public Page<KanjiInfo> getKanjiPage(int page, int size, KanjiFilterDdo filter) throws Exception {
        Pageable pageable = PageRequest.of(page, size);

        if (Objects.isNull(filter))
            return kanjiInfoRepository.findAll(pageable);
        return kanjiInfoRepository.findAll(getKanjiSpecByNonEmptyFilter(filter), pageable);
    }

    @Override
    public Map<KanjiInfo, String> getKanjiesByFilterWithWordRep(KanjiFilterDdo filter) throws Exception {
        List<KanjiInfo> kanjiInfos = getKanjiInfosByFilter(filter);
        List<String> kanjies = kanjiInfos.stream().map(KanjiInfo::getKanji).toList();

        Map<String, String> kanjiWordRepMap = kanjiInfoRepository.findKanjiWordRepresentations(kanjies).stream()
                 .collect(Collectors.toMap(row -> (String) row[0], row -> (String) row[1]));
        return kanjiInfos.stream().collect(Collectors.toMap(
                Function.identity(),
                kanjiInfo -> kanjiWordRepMap.get(kanjiInfo.getKanji())
        ));
    }

    @Override
    public Collection<KanjiInfo> getKanjiesByRadical(Set<String> radicals) {
        return kanjiInfoRepository.findKanjiesByRadicals(radicals.toArray(new String[0]));
    }


    private List<KanjiInfo> getKanjiInfosByFilter(KanjiFilterDdo filter) throws Exception {
        if (Objects.isNull(filter))
            return kanjiInfoRepository.findAll();
        return kanjiInfoRepository.findAll(getKanjiSpecByNonEmptyFilter(filter));
    }

    private Specification<KanjiInfo> getKanjiSpecByNonEmptyFilter(@NotNull KanjiFilterDdo filter) throws Exception {
        List<String> kanjiList = Collections.emptyList();
        if (Objects.nonNull(filter.getSearch()))
            kanjiList = searchRepository.findAllKanjiBySearch(filter.getSearch());

        return new KanjiInfoSpecificationsBuilder(
                Optional.ofNullable(filter.getGrade()).orElse(Collections.emptyList()),
                Optional.ofNullable(filter.getJlpt()).orElse(Collections.emptyList()),
                kanjiList
        ).build();
    }
}
