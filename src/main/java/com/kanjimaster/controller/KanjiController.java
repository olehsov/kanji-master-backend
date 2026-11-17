package com.kanjimaster.controller;

import com.kanjimaster.dto.KanjiFilterDto;
import com.kanjimaster.dto.KanjiInfoDto;
import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.service.KanjiInfoService;
import com.kanjimaster.service.Paging;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Controller
public class KanjiController {
    private final KanjiInfoService kanjiInfoService;

    public KanjiController(KanjiInfoService kanjiInfoService) {
        this.kanjiInfoService = kanjiInfoService;
    }

    @QueryMapping
    public Page<KanjiInfo> getKanjis(@Argument Integer page, @Argument Integer size, @Argument KanjiFilterDto filter) {
        return kanjiInfoService.getKanjiPage(Objects.requireNonNullElse(page, 0),
                Objects.requireNonNullElse(size, Paging.DEFAULT_SIZE), filter);
    }

    @QueryMapping
    public KanjiInfoDto getKanji(@Argument String kanji) {
        return kanjiInfoService.getKanji(kanji);
    }

    @QueryMapping
    public Collection<KanjiInfo> getKanjiesByRadical(@Argument Set<String> radicals) {
        return kanjiInfoService.getKanjiesByRadical(radicals);
    }
}
