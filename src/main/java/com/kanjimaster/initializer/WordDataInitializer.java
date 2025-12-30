package com.kanjimaster.initializer;

import com.kanjimaster.dto.KanjiFilterDdo;
import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.model.updated.Word;
import com.kanjimaster.service.KanjiInfoService;
import com.kanjimaster.service.WordService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Log4j2
public class WordDataInitializer {
    private final WordService wordService;

    public WordDataInitializer(WordService wordService) {
        this.wordService = wordService;
    }


    public void initialize() throws Exception {}
//
//    @Transactional
//    public void initialize() throws Exception {
//        List<Word> wordList = wordService.findAllWithNoTranslation();
//        while (true) {
//
//            List<Word> words = wordList.subList(0, Math.min(128, wordList.size()));
//            log.error("{} ITEM LEFT", wordList.size());
//            List<String> texts = words.stream().map(Word::getWord).toList();
//            List<String> translations = batchTranslate(texts, "ja", "uk");
//            log.error(translations);
//            for (int i = 0; i < words.size(); i++) {
//                words.get(i).setTranslation(translations.get(i));
//            }
//            wordService.save(words);
//            log.error("TEXTS {}, TRANSLATIONS {}", texts.size(), translations.size());
//
//            wordList.removeAll(new ArrayList<>(words));
//            if (wordList.isEmpty()) {
//                break;
//            }
//        }
//
//    }
//
//    public List<String> batchTranslate(List<String> texts, String sourceLang, String targetLang) {
//        RestTemplate restTemplate = new RestTemplate();
//        try {
//            Thread.sleep(1000); // Optional delay if throttling
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            return Collections.emptyList();
//        }
//
//        String url = "https://translation.googleapis.com/language/translate/v2?key=AIzaSyDb9fB8I5A2kHADM_CtEHlY8wzA71N2gb0";
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        texts.forEach(text -> body.add("q", text));
//        body.add("source", sourceLang);
//        body.add("target", targetLang);
//        body.add("format", "text");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
//        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
//
//        Map<String, Object> responseBody = response.getBody();
//
//        if (responseBody == null || !responseBody.containsKey("data")) return Collections.emptyList();
//
//        List<Map<String, Object>> translations = (List<Map<String, Object>>)
//                ((Map<String, Object>) responseBody.get("data")).get("translations");
//
//        return translations.stream()
//                .map(entry -> (String) entry.get("translatedText"))
//                .collect(Collectors.toList());
//    }
}
