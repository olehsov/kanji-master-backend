package com.kanjimaster.initializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanjimaster.model.Kanji;
import com.kanjimaster.service.KanjiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class KanjiDataInitializer {
    private final KanjiService kanjiService;

    public KanjiDataInitializer(KanjiService kanjiService) {
        this.kanjiService = kanjiService;
    }

    public void initialize() throws Exception {
        if (kanjiService.isKanjiTableEmpty()) {
            List<Kanji> kanjis = getKanjiFromJson();
            kanjiService.saveAll(kanjis);
        }
        log.debug("Kanji table already filled out");
    }

    private List<Kanji> getKanjiFromJson() {
        try {
            ClassPathResource resource = new ClassPathResource("static/fixture/kanji-fixture.json");
            File file = resource.getFile();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Map<String, Object>> kanjiJson = objectMapper.readValue(file, Map.class);
            Map<String, Object> kanjis = kanjiJson.get("kanjis");
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return kanjis.values().stream()
                    .map(kanji -> objectMapper.convertValue(kanji, Kanji.class))
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }
}
