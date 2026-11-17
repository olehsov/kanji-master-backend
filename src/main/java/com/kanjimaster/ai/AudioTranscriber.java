package com.kanjimaster.ai;

import java.nio.file.Path;

/** Speech-to-text: audio file to Japanese SRT. */
public interface AudioTranscriber {
    String transcribeToSrt(Path audioFile);
}
