package com.kanjimaster.ai.youtube;

import com.kanjimaster.ai.AiProperties;
import com.kanjimaster.exception.AiGenerationException;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Downloads a video's audio track as mp3 with {@code yt-dlp} into a fresh temp directory.
 * The caller owns the returned directory and must delete it (see {@link #deleteQuietly(Path)}).
 */
@Log4j2
public class YoutubeAudioDownloader {
    private final AiProperties.Youtube properties;

    public YoutubeAudioDownloader(AiProperties.Youtube properties) {
        this.properties = properties;
    }

    /** Returns the downloaded mp3. {@code videoId} must come from {@link YoutubeUrls#extractVideoId(String)}. */
    public Path download(String videoId, Path workDir) {
        // Only a validated id goes into the command, as a canonical URL after "--", so it can't be read as an option.
        List<String> command = List.of(properties.ytDlpCommand(), "-x", "--audio-format", "mp3",
                "--no-playlist", "-o", "audio-%(id)s.%(ext)s", "--", YoutubeUrls.canonicalUrl(videoId));
        try {
            Process process = new ProcessBuilder(command)
                    .directory(workDir.toFile())
                    .redirectErrorStream(true)
                    .redirectOutput(workDir.resolve("yt-dlp.log").toFile())
                    .start();
            if (!process.waitFor(properties.downloadTimeout().toMillis(), TimeUnit.MILLISECONDS)) {
                process.destroyForcibly();
                throw new AiGenerationException("Audio download timed out for video " + videoId);
            }
            if (process.exitValue() != 0) {
                throw new AiGenerationException("yt-dlp failed for video " + videoId + " (exit " + process.exitValue() + ")");
            }
        } catch (IOException e) {
            throw new AiGenerationException("Cannot run yt-dlp", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AiGenerationException("Audio download interrupted", e);
        }

        Path audio = workDir.resolve("audio-" + videoId + ".mp3");
        if (!Files.exists(audio)) {
            throw new AiGenerationException("yt-dlp produced no mp3 for video " + videoId);
        }
        return audio;
    }

    public static void deleteQuietly(Path dir) {
        if (dir == null) {
            return;
        }
        try (var paths = Files.walk(dir)) {
            paths.sorted((a, b) -> b.getNameCount() - a.getNameCount()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    log.warn("Cannot delete temp file {}", path);
                }
            });
        } catch (IOException e) {
            log.warn("Cannot clean temp dir {}", dir);
        }
    }
}
