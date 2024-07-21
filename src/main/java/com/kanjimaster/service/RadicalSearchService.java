package com.kanjimaster.service;

import com.kanjimaster.dto.RadicalSearchOptionDto;

import java.util.Collection;

public interface RadicalSearchService {
    Collection<RadicalSearchOptionDto> findAllRadicalsSearchOptions();
}
