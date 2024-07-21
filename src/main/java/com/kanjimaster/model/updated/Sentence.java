package com.kanjimaster.model.updated;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "SENTENCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sentence {
    @Id
    private Long id;
    private String japanese;
    private String english;
    private String particle;
    private String word;
    private String kanji;
    private String furigana;
    private String obi2;
}
