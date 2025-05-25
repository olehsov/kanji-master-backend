package com.kanjimaster.dto;


import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.model.updated.Word;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KanjiInfoDto {
    private String kanji;
    private String radical;
    private String radvar;
    private String type;
    private String regularOn;
    private String regularKun;
    private String onyomi;
    private String kunyomi;
    private String nanori;
    private Integer strokes;
    private String grade;
    private String jlpt;
    private String kanken;
    private Integer frequency;
    private String meaning;
    private String compactMeaning;
    private String elements;
    private String kanjiParts;
    private String partOf;
    private List<Word> words;

    public static KanjiInfoDto fromKanjiInfo(KanjiInfo entity) {
        List<Word> words = Objects.isNull(entity.getWords()) ?
                List.of() : entity.getWords().stream().limit(15).toList();
        return KanjiInfoDto.builder()
                .kanji(entity.getKanji())
                .radical(entity.getRadical())
                .radvar(entity.getRadvar())
                .type(entity.getType())
                .regularOn(entity.getRegularOn())
                .regularKun(entity.getRegularKun())
                .onyomi(entity.getOnyomi())
                .kunyomi(entity.getKunyomi())
                .nanori(entity.getNanori())
                .strokes(entity.getStrokes())
                .grade(entity.getGrade())
                .jlpt(entity.getJlpt())
                .kanken(entity.getKanken())
                .frequency(entity.getFrequency())
                .meaning(entity.getMeaning())
                .compactMeaning(entity.getCompactMeaning())
                .elements(entity.getElements())
                .kanjiParts(entity.getKanjiParts())
                .partOf(entity.getPartOf())
                .words(words)
                .build();
    }
}
