package com.kanjimaster.model.updated;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "kanji_word",
            joinColumns = @JoinColumn(name = "kanji"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    @OrderBy("freq DESC")
    private List<Word> words;
}
