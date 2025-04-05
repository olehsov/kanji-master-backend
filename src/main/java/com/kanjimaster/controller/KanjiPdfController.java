package com.kanjimaster.controller;

import com.kanjimaster.dto.KanjiFilterDdo;
import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.service.KanjiInfoService;
import com.kanjimaster.service.KanjiPdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/pdf")
public class KanjiPdfController {
    private final KanjiInfoService kanjiInfoService;
    private final KanjiPdfService kanjiPdfService;

    public KanjiPdfController(KanjiInfoService kanjiInfoService, KanjiPdfService kanjiPdfService) {
        this.kanjiInfoService = kanjiInfoService;
        this.kanjiPdfService = kanjiPdfService;
    }

    @PostMapping("/kanji-practice")
    public ResponseEntity<byte[]> generateKanjiPractice(@RequestBody KanjiFilterDdo filter) throws Exception {
        Map<KanjiInfo, String> kanjiesMap = kanjiInfoService.getKanjiesByFilterWithWordRep(filter);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "kanji_practice.pdf");

        return new ResponseEntity<>(kanjiPdfService.buildKanjiLearnFile(kanjiesMap), headers, HttpStatus.OK);
    }
}
