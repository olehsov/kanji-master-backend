package com.kanjimaster.service;

import com.kanjimaster.model.Reading;

import java.util.Collection;

public interface ReadingService {
    Collection<Reading> saveAll(Collection<Reading> readings);
    Collection<Reading> findAll();
    boolean isTableEmpty();
}
