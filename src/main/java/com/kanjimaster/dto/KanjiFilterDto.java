package com.kanjimaster.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Kanji filter shared by GraphQL {@code getKanjis} and the PDF endpoint.
 * {@code search} matches kanji typed directly (e.g. 人) and kana/romaji readings via the SEARCH table.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KanjiFilterDto {
    private String search;
    private List<String> grade;
    private List<String> jlpt;
}
