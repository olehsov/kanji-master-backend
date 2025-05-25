package com.kanjimaster.initializer;

import com.kanjimaster.service.WordService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class WordDataInitializer {
    private final WordService wordService;

    public WordDataInitializer(WordService wordService) {
        this.wordService = wordService;
    }

    public void initialize() throws Exception {}

//    @Transactional
//    public void initialize() throws Exception {
//        int page = 0;
//        while (true) {
//            List<Word> words = wordService.getWordPage(page, 128).getContent();
//            log.error("PAGE {} ITEMS COMPLETED {} OF 100553", page, (page + 1) * 128);
//            if (words.isEmpty()) {
//                break;
//            }
//            List<String> texts = words.stream().map(Word::getWord).toList();
//            List<String> translations = batchTranslate(texts, "ja", "uk");
//            log.error(translations);
//            for (int i = 0; i < words.size(); i++) {
//                words.get(i).setTranslation(translations.get(i));
//            }
//            wordService.save(words);
//            page += 1;
//            log.error("TEXTS {}, TRANSLATIONS {}", texts.size(), translations.size());
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
