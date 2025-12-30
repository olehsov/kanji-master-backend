package com.kanjimaster.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "INDIVIDUAL_SUBTITLE_TRANSLATION")
@Data
public class IndividualSubtitleTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private String textHiragana;
    private String meaning;
    private boolean isWord;
    private String jlpt;
    @Column(name = "subtitle_id", insertable = false, updatable = false)
    private Long subtitleId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "subtitle_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private VideoSubtitle videoSubtitle;
}
