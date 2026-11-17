package com.kanjimaster.ai.openai;

import com.kanjimaster.ai.AudioTranscriber;

import java.nio.file.Path;

/** Speech-to-text through OpenAI Whisper. Claude has no audio input, so this stays on OpenAI. */
public class OpenAiWhisperTranscriber implements AudioTranscriber {
    private final OpenAiHttpClient client;

    public OpenAiWhisperTranscriber(OpenAiHttpClient client) {
        this.client = client;
    }

    @Override
    public String transcribeToSrt(Path audioFile) {
        return client.transcribeToSrt(audioFile);
    }
}
