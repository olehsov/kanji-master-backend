package com.kanjimaster.model.updated;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "WORD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word")
    private String word;

    @Column(name = "yomi_display")
    private String yomiDisplay;

    @Column(name = "romaji_display")
    private String romajiDisplay;

    @Column(name = "freq")
    private int freq;

    @Column(name = "is_self_supporting")
    private boolean isSelfSupporting;
}