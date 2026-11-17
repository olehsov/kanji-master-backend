package com.kanjimaster.service;

import com.kanjimaster.exception.BadRequestException;
import org.springframework.data.domain.PageRequest;

/** Validates client paging input. */
public final class Paging {
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 100;

    private Paging() {
    }

    public static PageRequest of(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("page must be >= 0");
        }
        if (size < 1 || size > MAX_SIZE) {
            throw new BadRequestException("size must be between 1 and " + MAX_SIZE);
        }
        return PageRequest.of(page, size);
    }
}
