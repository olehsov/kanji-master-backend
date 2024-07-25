package com.kanjimaster.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KanjiFilterDdo {
    private String search;
    private List<String> grade;
    private List<String> jlpt;
}
