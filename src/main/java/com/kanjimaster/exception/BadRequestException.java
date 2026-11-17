package com.kanjimaster.exception;

/** Client sent invalid input. Mapped to GraphQL BAD_REQUEST / HTTP 400. */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
