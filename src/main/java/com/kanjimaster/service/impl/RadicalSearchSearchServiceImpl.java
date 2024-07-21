package com.kanjimaster.service.impl;

import com.kanjimaster.dto.RadicalSearchOptionDto;
import com.kanjimaster.model.updated.Radical;
import com.kanjimaster.model.updated.Radvar;
import com.kanjimaster.service.RadVarService;
import com.kanjimaster.service.RadicalSearchService;
import com.kanjimaster.service.RadicalService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RadicalSearchSearchServiceImpl implements RadicalSearchService {
    private final RadicalService radicalService;
    private final RadVarService radVarService;

    public RadicalSearchSearchServiceImpl(RadicalService radicalService, RadVarService radVarService) {
        this.radicalService = radicalService;
        this.radVarService = radVarService;
    }

    @Override
    public Collection<RadicalSearchOptionDto> findAllRadicalsSearchOptions() {
        Collection<Radical> radicals =  radicalService.findAllRadicals();
        Collection<Radvar> radVars = radVarService.findAllRadVars();
        return Stream.concat(radicals.stream(), radVars.stream())
                .map(RadicalSearchOptionDto::fromBaseRadical)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(RadicalSearchOptionDto::getStrokes))
                .collect(Collectors.toList());
    }
}
