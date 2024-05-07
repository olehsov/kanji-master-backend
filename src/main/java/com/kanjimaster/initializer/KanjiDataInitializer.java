package com.kanjimaster.initializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanjimaster.mappers.KanjiMapper;
import com.kanjimaster.mappers.ReadingMapper;
import com.kanjimaster.mappers.WordMapper;
import com.kanjimaster.model.Kanji;
import com.kanjimaster.model.Reading;
import com.kanjimaster.service.KanjiService;
import com.kanjimaster.service.ReadingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class KanjiDataInitializer {
    private final KanjiService kanjiService;
    private final ReadingService readingService;

    public KanjiDataInitializer(KanjiService kanjiService, ReadingService readingService) {
        this.kanjiService = kanjiService;
        this.readingService = readingService;
    }

    public void initialize() throws Exception {
        initializeReadings();
        initializeKanjies();
        log.debug("Initialization successfully passed");
    }

    private void initializeReadings() {
        if (!readingService.isTableEmpty())
            return;

        List<ReadingMapper> readingMappers = getListFromFixture("readings", ReadingMapper.class);
        Set<Reading> readings = readingMappers.parallelStream()
                .map(ReadingMapper::convertToEntity)
                .collect(Collectors.toSet());

        readingService.saveAll(readings);
    }

    private void initializeKanjies() {
        if (!this.kanjiService.isTableEmpty())
            return;
        List<KanjiMapper> kanjiMappers = getListFromFixture("kanjis", KanjiMapper.class);
        List<WordMapper> wordMappers = getListFromFixture();

        Collection<Reading> readings = readingService.findAll();

        kanjiMappers.parallelStream().forEach(kanjiMapper -> {
            Kanji kanji = kanjiMapper.convertToEntity(wordMappers);
            log.info("Kanji {} has been successfully built.", kanji.getKanji());
            kanjiService.save(kanji);

            kanjiMapper.updateReadings(readings, kanji);
            kanjiService.save(kanji);
            log.info("Kanji {} with id {} has been successfully created.", kanji.getId(), kanji.getKanji());
        });
    }

    private <T> List<T> getListFromFixture(String fileName, Class<T> toValueType) {
        try {
            File file = this.getFixtureFile(fileName);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, LinkedHashMap<?, ?>> values = objectMapper.readValue(file, Map.class);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return values.values().stream()
                    .map(kanji -> objectMapper.convertValue(kanji, toValueType))
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }
    private List<WordMapper> getListFromFixture() {
        try {
            File file = this.getFixtureFile("words");

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, List<LinkedHashMap<?, ?>>> values = objectMapper.readValue(file, Map.class);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return values.keySet().stream()
                    .flatMap(key -> {
                        List<LinkedHashMap<?, ?>> words = values.get(key);
                        List<WordMapper> wordMappers =  words.stream()
                                .map(value -> objectMapper.convertValue(value, WordMapper.class))
                                .toList();
                        wordMappers.forEach(wordMapper -> wordMapper.setKanji(key));
                        return wordMappers.stream();
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    private File getFixtureFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(
                String.format("static/fixture/%s-fixture.json", fileName)
        );
        return resource.getFile();
    }
}
