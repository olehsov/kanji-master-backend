package com.kanjimaster.controller;

import com.kanjimaster.model.Word;
import com.kanjimaster.service.WordService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class WordController {
    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @QueryMapping
    public Mono<Page<Word>> getWordsByKanji(@Argument int page, @Argument int size, @Argument Long kanjiId) {
        return Mono.just(wordService.getWordByKanjiPage(page, size, kanjiId));
    }

    @QueryMapping
    public Mono<Page<Word>> getWords(@Argument int page, @Argument int size) {
        return Mono.just(wordService.getWordPage(page, size));
    }
}
