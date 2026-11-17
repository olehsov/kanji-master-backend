package com.kanjimaster.controller;

import com.kanjimaster.dto.KanjiFilterDto;
import com.kanjimaster.exception.NotFoundException;
import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.service.KanjiInfoService;
import com.kanjimaster.service.KanjiPdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/pdf")
@Tag(name = "PDF", description = "Printable kanji practice sheets")
public class KanjiPdfController {
    private final KanjiInfoService kanjiInfoService;
    private final KanjiPdfService kanjiPdfService;

    public KanjiPdfController(KanjiInfoService kanjiInfoService, KanjiPdfService kanjiPdfService) {
        this.kanjiInfoService = kanjiInfoService;
        this.kanjiPdfService = kanjiPdfService;
    }

    @PostMapping(value = "/kanji-practice", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Generate a kanji writing practice PDF",
            description = "One block per kanji matching the filter (meanings, readings, top words and a writing grid), "
                    + "ordered by frequency.")
    @ApiResponse(responseCode = "200", description = "PDF file", content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE))
    @ApiResponse(responseCode = "404", description = "No kanji match the filter")
    public ResponseEntity<byte[]> generateKanjiPractice(@RequestBody KanjiFilterDto filter) throws IOException {
        Map<KanjiInfo, String> kanjiesMap = kanjiInfoService.getKanjiesByFilterWithWordRep(filter);
        if (kanjiesMap.isEmpty()) {
            throw new NotFoundException("No kanji match the filter");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("kanji_practice.pdf").build().toString())
                .body(kanjiPdfService.buildKanjiLearnFile(kanjiesMap));
    }
}
