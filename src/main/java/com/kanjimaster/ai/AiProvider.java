package com.kanjimaster.ai;

/** Which backend produces AI content. Selected with {@code kanji.ai.provider}. */
public enum AiProvider {
    /** Fixed pre-generated responses; no external calls. Default. */
    STUB,
    /** Claude (Anthropic API) for text, OpenAI Whisper for audio transcription. */
    CLAUDE,
    /** Legacy OpenAI chat + Whisper implementation. */
    @Deprecated
    OPENAI
}
