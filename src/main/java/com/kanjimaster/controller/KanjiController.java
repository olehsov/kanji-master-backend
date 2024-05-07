package com.kanjimaster.controller;

import com.kanjimaster.filter.kanji.KanjiFilter;
import com.kanjimaster.model.Kanji;
import com.kanjimaster.service.KanjiService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class KanjiController {
    private final KanjiService kanjiService;

    public KanjiController(KanjiService kanjiService) {
        this.kanjiService = kanjiService;
    }

    @QueryMapping
    public Mono<Page<Kanji>> getKanjis(@Argument int page, @Argument int size, @Argument KanjiFilter filter) throws Exception {
        return Mono.just(kanjiService.getKanjiPage(page, size, filter));
    }

    @QueryMapping
    public Mono<Kanji> getKanji(@Argument Long id) {
        return Mono.just(kanjiService.getKanji(id));
    }
}
