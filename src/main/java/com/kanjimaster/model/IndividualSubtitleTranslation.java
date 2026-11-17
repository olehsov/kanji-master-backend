package com.kanjimaster.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "INDIVIDUAL_SUBTITLE_TRANSLATION")
@Getter
@Setter
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
    private VideoSubtitle videoSubtitle;
}
