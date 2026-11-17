package com.kanjimaster.ai;

import com.kanjimaster.ai.youtube.YoutubeAudioDownloader;
import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.exception.AiGenerationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Real subtitle pipeline: yt-dlp audio download, then speech-to-text, then AI formatting/translation.
 */
@Component
@ConditionalOnExpression("'${kanji.ai.provider:stub}' != 'stub'")
public class PipelineSubtitleGenerator implements SubtitleGenerator {
    private final YoutubeAudioDownloader downloader;
    private final AudioTranscriber transcriber;
    private final SubtitleFormatter formatter;

    public PipelineSubtitleGenerator(AiProperties properties, AudioTranscriber transcriber, SubtitleFormatter formatter) {
        this.downloader = new YoutubeAudioDownloader(properties.youtube());
        this.transcriber = transcriber;
        this.formatter = formatter;
    }

    @Override
    public SubtitleWrapDto generate(String youtubeId) {
        Path workDir = null;
        try {
            workDir = Files.createTempDirectory("kanji-audio-");
            Path audio = downloader.download(youtubeId, workDir);
            String srt = transcriber.transcribeToSrt(audio);
            return formatter.format(srt);
        } catch (IOException e) {
            throw new AiGenerationException("Cannot create temp directory for audio", e);
        } finally {
            YoutubeAudioDownloader.deleteQuietly(workDir);
        }
    }
}
