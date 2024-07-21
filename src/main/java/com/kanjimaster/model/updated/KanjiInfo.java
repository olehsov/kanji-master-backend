package com.kanjimaster.model.updated;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "KANJI_INFO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KanjiInfo {
    @Id
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
}
