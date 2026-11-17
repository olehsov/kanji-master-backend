package com.kanjimaster.exception;

/** Requested resource does not exist. Mapped to GraphQL NOT_FOUND / HTTP 404. */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
