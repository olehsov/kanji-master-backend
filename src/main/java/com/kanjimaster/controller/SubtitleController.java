package com.kanjimaster.controller;

import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.service.SubtitleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subtitle")
@Tag(name = "Subtitles", description = "Japanese subtitles with Ukrainian translation for YouTube videos")
public class SubtitleController {
    private final SubtitleService subtitleService;

    public SubtitleController(SubtitleService subtitleService) {
        this.subtitleService = subtitleService;
    }

    @PostMapping("/extract-youtube-video")
    @Operation(summary = "Get study subtitles for a YouTube video",
            description = "Returns cached subtitles, or generates them with the active AI provider. "
                    + "With the default `stub` provider a fixed demo response is returned.")
    @ApiResponse(responseCode = "200", description = "Subtitles sorted by index")
    @ApiResponse(responseCode = "400", description = "URL is not a YouTube video link")
    public SubtitleWrapDto extractYoutubeVideo(@Valid @RequestBody SubtitleYoutubeDto request) {
        return subtitleService.extract(request.youtubeUrl());
    }

    /** Request body: a YouTube video link (watch, youtu.be, shorts, embed). */
    public record SubtitleYoutubeDto(@NotBlank String youtubeUrl) {
    }
}
