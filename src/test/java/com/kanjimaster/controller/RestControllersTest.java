package com.kanjimaster.controller;

import com.kanjimaster.dto.SubtitleDto;
import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.exception.BadRequestException;
import com.kanjimaster.service.KanjiInfoService;
import com.kanjimaster.service.KanjiPdfService;
import com.kanjimaster.service.SubtitleService;
import com.kanjimaster.support.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({KanjiPdfController.class, SubtitleController.class})
class RestControllersTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KanjiInfoService kanjiInfoService;
    @MockitoBean
    private KanjiPdfService kanjiPdfService;
    @MockitoBean
    private SubtitleService subtitleService;

    @Test
    void pdfIsReturnedAsAttachment() throws Exception {
        when(kanjiInfoService.getKanjiesByFilterWithWordRep(any())).thenReturn(Map.of(TestData.kanji("日", 1), ""));
        when(kanjiPdfService.buildKanjiLearnFile(anyMap())).thenReturn("%PDF-1.7".getBytes());

        mockMvc.perform(post("/pdf/kanji-practice").contentType(MediaType.APPLICATION_JSON).content("{\"jlpt\":[\"5\"]}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"kanji_practice.pdf\""))
                .andExpect(content().bytes("%PDF-1.7".getBytes()));
    }

    @Test
    void pdfWithNoMatchingKanjiIs404ProblemDetail() throws Exception {
        when(kanjiInfoService.getKanjiesByFilterWithWordRep(any())).thenReturn(Map.of());

        mockMvc.perform(post("/pdf/kanji-practice").contentType(MediaType.APPLICATION_JSON).content("{\"search\":\"zzz\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No kanji match the filter"));
    }

    @Test
    void subtitlesAreReturned() throws Exception {
        SubtitleWrapDto dto = new SubtitleWrapDto(List.of(SubtitleDto.builder().index(1).line("猫").translation("кіт")
                .individualTranslations(List.of()).build()));
        when(subtitleService.extract("https://youtu.be/qal34e9v_pk")).thenReturn(dto);

        mockMvc.perform(post("/api/subtitle/extract-youtube-video").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"youtubeUrl\":\"https://youtu.be/qal34e9v_pk\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subtitles[0].line").value("猫"))
                .andExpect(jsonPath("$.subtitles[0].translation").value("кіт"));
    }

    @Test
    void invalidVideoUrlIs400() throws Exception {
        when(subtitleService.extract("https://evil.com")).thenThrow(new BadRequestException("Not a valid YouTube video URL"));

        mockMvc.perform(post("/api/subtitle/extract-youtube-video").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"youtubeUrl\":\"https://evil.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Not a valid YouTube video URL"));
    }

    @Test
    void blankVideoUrlFailsValidation() throws Exception {
        mockMvc.perform(post("/api/subtitle/extract-youtube-video").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"youtubeUrl\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
