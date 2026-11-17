package com.kanjimaster.controller;

import com.kanjimaster.model.updated.Sentence;
import com.kanjimaster.service.Paging;
import com.kanjimaster.service.SentenceService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class SentenceController {
    private final SentenceService service;

    public SentenceController(SentenceService service) {
        this.service = service;
    }

    @QueryMapping
    public Page<Sentence> getSentencesByKanji(@Argument Integer page, @Argument Integer size, @Argument String kanji) {
        return service.findSentencesByKanji(Objects.requireNonNullElse(page, 0),
                Objects.requireNonNullElse(size, Paging.DEFAULT_SIZE), kanji);
    }
}
