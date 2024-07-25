package com.kanjimaster.controller;

import com.kanjimaster.model.updated.Sentence;
import com.kanjimaster.service.SentenceService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class SentenceController {
    private final SentenceService service;

    public SentenceController(SentenceService service) {
        this.service = service;
    }

    @QueryMapping
    public Mono<Page<Sentence>> getSentencesByKanji(@Argument int page, @Argument int size, @Argument String kanji) {
        return Mono.just(service.findSentencesByKanji(page, size, kanji));
    }
}
