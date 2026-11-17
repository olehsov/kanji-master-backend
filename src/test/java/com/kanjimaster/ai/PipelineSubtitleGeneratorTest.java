package com.kanjimaster.ai;

import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.exception.AiGenerationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Runs the pipeline with a fake yt-dlp shell script instead of the real binary. */
@DisabledOnOs(OS.WINDOWS)
class PipelineSubtitleGeneratorTest {
    @TempDir
    Path scripts;

    private Path fakeYtDlp(String body) throws IOException {
        Path script = scripts.resolve("yt-dlp-" + System.nanoTime() + ".sh");
        Files.writeString(script, "#!/bin/sh\n" + body + "\n");
        Files.setPosixFilePermissions(script, PosixFilePermissions.fromString("rwxr-xr-x"));
        return script;
    }

    private static AiProperties properties(Path ytDlp, Duration timeout) {
        return new AiProperties(AiProvider.CLAUDE, null, null,
                new AiProperties.Youtube(ytDlp.toString(), timeout));
    }

    @Test
    void downloadsTranscribesFormatsAndCleansUp() throws IOException {
        // The last argument is the canonical URL; record all args to check the id is passed safely.
        Path args = scripts.resolve("args.txt");
        Path ytDlp = fakeYtDlp("echo \"$@\" > " + args + "\necho audio > audio-qal34e9v_pk.mp3");
        AtomicReference<Path> transcribed = new AtomicReference<>();
        List<String> formatted = new ArrayList<>();
        SubtitleWrapDto expected = new SubtitleWrapDto(List.of());

        PipelineSubtitleGenerator generator = new PipelineSubtitleGenerator(properties(ytDlp, Duration.ofSeconds(30)),
                audio -> {
                    transcribed.set(audio);
                    assertThat(audio).exists();
                    return "1\n00:00:01,000 --> 00:00:02,000\n猫\n";
                },
                srt -> {
                    formatted.add(srt);
                    return expected;
                });

        assertThat(generator.generate("qal34e9v_pk")).isSameAs(expected);
        assertThat(formatted).singleElement().asString().contains("猫");
        assertThat(Files.readString(args)).endsWith("-- https://www.youtube.com/watch?v=qal34e9v_pk\n");
        assertThat(transcribed.get()).doesNotExist();
        assertThat(transcribed.get().getParent()).doesNotExist();
    }

    @Test
    void failedDownloadIsReported() throws IOException {
        Path ytDlp = fakeYtDlp("exit 3");
        PipelineSubtitleGenerator generator = new PipelineSubtitleGenerator(properties(ytDlp, Duration.ofSeconds(30)),
                audio -> "", srt -> null);

        assertThatThrownBy(() -> generator.generate("qal34e9v_pk"))
                .isInstanceOf(AiGenerationException.class).hasMessageContaining("exit 3");
    }

    @Test
    void missingAudioIsReported() throws IOException {
        Path ytDlp = fakeYtDlp("exit 0");
        PipelineSubtitleGenerator generator = new PipelineSubtitleGenerator(properties(ytDlp, Duration.ofSeconds(30)),
                audio -> "", srt -> null);

        assertThatThrownBy(() -> generator.generate("qal34e9v_pk"))
                .isInstanceOf(AiGenerationException.class).hasMessageContaining("no mp3");
    }

    @Test
    void slowDownloadTimesOut() throws IOException {
        Path ytDlp = fakeYtDlp("sleep 5");
        PipelineSubtitleGenerator generator = new PipelineSubtitleGenerator(properties(ytDlp, Duration.ofMillis(300)),
                audio -> "", srt -> null);

        assertThatThrownBy(() -> generator.generate("qal34e9v_pk"))
                .isInstanceOf(AiGenerationException.class).hasMessageContaining("timed out");
    }
}
