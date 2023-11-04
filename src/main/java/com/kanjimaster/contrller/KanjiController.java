package com.kanjimaster.contrller;

import com.kanjimaster.model.Kanji;
import com.kanjimaster.service.KanjiService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class KanjiController {
    private final KanjiService kanjiService;

    public KanjiController(KanjiService kanjiService) {
        this.kanjiService = kanjiService;
    }

    @QueryMapping
    public Mono<Page<Kanji>> getKanjies(@Argument int page, @Argument int size) {
        return Mono.just(kanjiService.getKanjiPage(page, size));
    }

    @QueryMapping
    public Mono<Kanji> getKanji(@Argument Long id) {
        return Mono.just(kanjiService.getKanji(id));
    }
}
