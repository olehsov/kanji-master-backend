package com.kanjimaster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanjimaster.client.OpenAiClientService;
import com.kanjimaster.dto.SubtitleDto;
import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.model.VideoMetadata;
import com.kanjimaster.service.VideoMetadataService;
import lombok.Data;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/subtitle")
public class SubtitleController {

    private final OpenAiClientService openAiClientService;
    private final VideoMetadataService videoMetadataService;
    private final ObjectMapper objectMapper;
    private static final String VIDEO_NAME = "audio-%(id)s.%(ext)s";

    public SubtitleController(OpenAiClientService openAiClientService, VideoMetadataService videoMetadataService) {
        this.openAiClientService = openAiClientService;
        this.videoMetadataService = videoMetadataService;
        this.objectMapper = new ObjectMapper();;
    }

    @PostMapping("/extract-youtube-video")
    public ResponseEntity<SubtitleWrapDto> extractYoutubeVideo(@RequestBody SubtitleYoutubeDto urlDto) {
        try {
            String youtubeUrl = urlDto.getYoutubeUrl();
            String youtubeId = extractVideoId(youtubeUrl);

            SubtitleWrapDto subtitleWrapDto = videoMetadataService.findByYoutubeId(youtubeId);

            if (Objects.isNull(subtitleWrapDto)) {
                ProcessBuilder pb = new ProcessBuilder("yt-dlp", "-x", "--audio-format", "mp3", youtubeUrl, "-o", VIDEO_NAME);
                pb.directory(new File("/tmp"));
                pb.inheritIO().start().waitFor();

                File dir = new File("/tmp");
                File[] mp3Files = dir.listFiles((d, name) -> name.matches("audio-" + youtubeId + "\\.mp3"));
                if (mp3Files == null || mp3Files.length == 0) {
                    throw new RuntimeException("No MP3 file found in /tmp");
                }
                File audioFile = mp3Files[0];

                Resource resource = new FileSystemResource(audioFile);

                String result = openAiClientService.processVideoToVideoTranscription(resource);

                subtitleWrapDto = openAiClientService.getSubtitleWrapDto(result);

                videoMetadataService.createYoutubeVideo(subtitleWrapDto, youtubeId);
            }

            List<SubtitleDto> subtitles = subtitleWrapDto.getSubtitles().stream().sorted(Comparator.comparingInt(SubtitleDto::getIndex)).toList();
            subtitleWrapDto.setSubtitles(subtitles);
            return ResponseEntity.ok().body(subtitleWrapDto);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractVideoId(String url) {
        String pattern = "^(?:https?://)?(?:www\\.|m\\.)?(?:youtube\\.com|youtu\\.be)/(?:watch\\?v=|embed/|v/)?([\\w-]{11})";
        Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Data
    public static final class SubtitleYoutubeDto {
        private String youtubeUrl;
    }
}