package com.kanjimaster.controller;

import com.kanjimaster.client.OpenAiClientService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private static final String AUDIO_FORMAT = "mp3"; // or "wav"
    private final OpenAiClientService openAiClientService;

    public MediaController(OpenAiClientService openAiClientService) {
        this.openAiClientService = openAiClientService;
    }

    @PostMapping("/extract-to-media")
    public ResponseEntity<String> extractAudio(@RequestParam("file") MultipartFile videoFile) {
        try {
            // Define root folder (project root)
            Path rootPath = new File(".").getCanonicalFile().toPath();

            String[] splitName = videoFile.getOriginalFilename().split("\\.");
            String extension = splitName[splitName.length - 1];
            String videoName = UUID.randomUUID() + "." + extension;

            // Save uploaded video to root folder
            File inputVideo = new File(rootPath.toFile(), videoName);
            videoFile.transferTo(inputVideo);

            // Prepare audio file name and output path
            String audioFileName = UUID.randomUUID() + "." + AUDIO_FORMAT;
            File audioFile = new File(rootPath.toFile(), audioFileName);

            // Run ffmpeg to extract audio
            ProcessBuilder builder = new ProcessBuilder(
                    "ffmpeg", "-i", inputVideo.getAbsolutePath(),
                    "-vn", "-acodec", "libmp3lame",
                    audioFile.getAbsolutePath()
            );
            builder.redirectErrorStream(true);
            Process process = builder.start();
            process.waitFor();

            if (!audioFile.exists()) {
                return ResponseEntity.badRequest().body("Failed to extract audio.");
            }

            Resource resource = new FileSystemResource(audioFile);
            String result = openAiClientService.processVideoToVideoTranscription(resource);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }
}