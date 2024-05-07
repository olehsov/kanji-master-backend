package com.kanjimaster.service.impl;

import com.kanjimaster.model.Reading;
import com.kanjimaster.repository.ReadingRepository;
import com.kanjimaster.service.ReadingService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ReadingServiceImpl implements ReadingService {
    private final ReadingRepository readingRepository;

    public ReadingServiceImpl(ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    @Override
    public Collection<Reading> saveAll(Collection<Reading> readings) {
        return readingRepository.saveAll(readings);
    }

    @Override
    public Collection<Reading> findAll() {
        return readingRepository.findAll();
    }

    @Override
    public boolean isTableEmpty() {
        return readingRepository.count() == 0;
    }
}
