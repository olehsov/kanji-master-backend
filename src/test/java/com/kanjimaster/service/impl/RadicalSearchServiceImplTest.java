package com.kanjimaster.service.impl;

import com.kanjimaster.dto.RadicalSearchOptionDto;
import com.kanjimaster.model.updated.Radical;
import com.kanjimaster.model.updated.Radvar;
import com.kanjimaster.service.RadVarService;
import com.kanjimaster.service.RadicalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RadicalSearchServiceImplTest {
    @Mock
    private RadicalService radicalService;
    @Mock
    private RadVarService radVarService;
    @InjectMocks
    private RadicalSearchServiceImpl service;

    @Test
    void mergesRadicalsAndVariantsSortedByStrokes() {
        when(radicalService.findAllRadicals()).thenReturn(List.of(
                Radical.builder().radical("水").strokes(4).build(),
                Radical.builder().radical("一").strokes(1).build()));
        when(radVarService.findAllRadVars()).thenReturn(List.of(Radvar.builder().radvar("氵").strokes(3).build()));

        assertThat(service.findAllRadicalsSearchOptions())
                .extracting(RadicalSearchOptionDto::getRadical, RadicalSearchOptionDto::getStrokes,
                        RadicalSearchOptionDto::isSimplifiedRadical)
                .containsExactly(tuple("一", 1, false), tuple("氵", 3, true), tuple("水", 4, false));
    }
}
