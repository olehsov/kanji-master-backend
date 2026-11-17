package com.kanjimaster.service.impl;

import com.kanjimaster.dto.KanjiFilterDto;
import com.kanjimaster.dto.KanjiInfoDto;
import com.kanjimaster.exception.BadRequestException;
import com.kanjimaster.exception.NotFoundException;
import com.kanjimaster.filter.KanjiInfoSpecifications;
import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.repository.KanjiInfoRepository;
import com.kanjimaster.repository.SearchRepository;
import com.kanjimaster.service.KanjiInfoService;
import com.kanjimaster.service.Paging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class KanjiInfoServiceImpl implements KanjiInfoService {
    private final KanjiInfoRepository kanjiInfoRepository;
    private final SearchRepository searchRepository;

    public KanjiInfoServiceImpl(KanjiInfoRepository kanjiInfoRepository, SearchRepository searchRepository) {
        this.kanjiInfoRepository = kanjiInfoRepository;
        this.searchRepository = searchRepository;
    }

    @Override
    public KanjiInfoDto getKanji(String kanji) {
        if (kanji == null || kanji.isBlank()) {
            throw new BadRequestException("Kanji must not be empty");
        }
        return kanjiInfoRepository.findById(kanji)
                .map(KanjiInfoDto::fromKanjiInfo)
                .orElseThrow(() -> new NotFoundException("Kanji '" + kanji + "' not found"));
    }

    @Override
    public Page<KanjiInfo> getKanjiPage(int page, int size, KanjiFilterDto filter) {
        PageRequest pageable = Paging.of(page, size);
        return toSpecification(filter)
                .map(spec -> kanjiInfoRepository.findAll(spec, pageable))
                .orElseGet(() -> Page.empty(pageable));
    }

    @Override
    public Map<KanjiInfo, String> getKanjiesByFilterWithWordRep(KanjiFilterDto filter) {
        List<KanjiInfo> kanjiInfos = toSpecification(filter)
                .map(kanjiInfoRepository::findAll)
                .orElseGet(List::of);
        if (kanjiInfos.isEmpty()) {
            return Map.of();
        }

        List<String> kanjies = kanjiInfos.stream().map(KanjiInfo::getKanji).toList();
        Map<String, String> wordRepByKanji = kanjiInfoRepository.findKanjiWordRepresentations(kanjies).stream()
                .collect(Collectors.toMap(row -> (String) row[0], row -> (String) row[1]));

        // LinkedHashMap: Collectors.toMap rejects null values (kanji without words).
        Map<KanjiInfo, String> result = new LinkedHashMap<>();
        kanjiInfos.forEach(info -> result.put(info, wordRepByKanji.getOrDefault(info.getKanji(), "")));
        return result;
    }

    @Override
    public Collection<KanjiInfo> getKanjiesByRadical(Set<String> radicals) {
        if (radicals == null || radicals.isEmpty()) {
            return List.of();
        }
        return kanjiInfoRepository.findKanjiesByRadicals(radicals.toArray(String[]::new));
    }

    /**
     * Builds the filter specification. Returns empty when a search term is given but matches nothing,
     * so callers return no results instead of the whole table.
     */
    private Optional<Specification<KanjiInfo>> toSpecification(KanjiFilterDto filter) {
        if (filter == null) {
            return Optional.of(Specification.unrestricted());
        }

        Specification<KanjiInfo> spec = KanjiInfoSpecifications.attributeIn("grade", filter.getGrade())
                .and(KanjiInfoSpecifications.attributeIn("jlpt", filter.getJlpt()));

        String search = filter.getSearch();
        if (search != null && !search.isBlank()) {
            String term = search.trim();
            // Kanji typed directly (e.g. "人" or "日本") match themselves; the rest matches readings.
            Set<String> matches = new LinkedHashSet<>(kanjiCharacters(term));
            matches.addAll(searchRepository.findAllKanjiBySearch(term));
            if (matches.isEmpty()) {
                return Optional.empty();
            }
            spec = spec.and(KanjiInfoSpecifications.attributeIn("kanji", matches));
        }
        return Optional.of(spec);
    }

    /** Han (kanji) characters contained in the term, in order, without duplicates. */
    static List<String> kanjiCharacters(String term) {
        return term.codePoints()
                .filter(cp -> Character.UnicodeScript.of(cp) == Character.UnicodeScript.HAN)
                .distinct()
                .mapToObj(Character::toString)
                .toList();
    }
}
