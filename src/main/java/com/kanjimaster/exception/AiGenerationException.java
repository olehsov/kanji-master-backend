package com.kanjimaster.exception;

/** An AI provider failed or returned unusable output. Mapped to GraphQL INTERNAL_ERROR / HTTP 502. */
public class AiGenerationException extends RuntimeException {
    public AiGenerationException(String message) {
        super(message);
    }

    public AiGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
