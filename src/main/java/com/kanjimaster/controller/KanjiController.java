package com.kanjimaster.controller;

import com.kanjimaster.dto.KanjiFilterDdo;
import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.service.KanjiInfoService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;

@Controller
public class KanjiController {
    private final KanjiInfoService kanjiInfoService;

    public KanjiController(KanjiInfoService kanjiInfoService) {
        this.kanjiInfoService = kanjiInfoService;
    }

    @QueryMapping
    public Mono<Page<KanjiInfo>> getKanjis(@Argument int page, @Argument int size, @Argument KanjiFilterDdo filter) throws Exception {
        return Mono.just(kanjiInfoService.getKanjiPage(page, size, filter));
    }

    @QueryMapping
    public Mono<KanjiInfo> getKanji(@Argument String kanji) {
        return Mono.just(kanjiInfoService.getKanji(kanji));
    }

    @QueryMapping
    public Mono<Collection<KanjiInfo>> getKanjiesByRadical(@Argument Set<String> radicals) {
        return Mono.just(kanjiInfoService.getKanjiesByRadical(radicals));
    }
}
