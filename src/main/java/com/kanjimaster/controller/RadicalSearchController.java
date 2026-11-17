package com.kanjimaster.controller;

import com.kanjimaster.dto.RadicalSearchOptionDto;
import com.kanjimaster.service.RadicalSearchService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;

@Controller
public class RadicalSearchController {
    private final RadicalSearchService radicalSearchService;

    public RadicalSearchController(RadicalSearchService radicalSearchService) {
        this.radicalSearchService = radicalSearchService;
    }

    @QueryMapping
    public Collection<RadicalSearchOptionDto> getRadicalSearchOptionDto() {
        return radicalSearchService.findAllRadicalsSearchOptions();
    }
}
