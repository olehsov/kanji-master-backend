package com.kanjimaster.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "KANJI")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Kanji {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kanji;
    private int grade;
    @JsonProperty(value = "stroke_count")
    private Integer strokeCount;
    @ManyToMany(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinTable(
            name = "KANJI_KUN_READING",
            joinColumns = {
                    @JoinColumn(name = "reading_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "kanji_id")
            }
    )
    private Set<Reading> kunReadings;
    @ManyToMany(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinTable(
            name = "KANJI_ON_READING",
            joinColumns = {
                    @JoinColumn(name = "reading_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "kanji_id")
            }
    )
    private Set<Reading> onReadings;
    @ManyToMany(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinTable(
            name = "KANJI_NAME_READING",
            joinColumns = {
                    @JoinColumn(name = "reading_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "kanji_id")
            }
    )
    private Set<Reading> nameReadings;
    @OneToMany(mappedBy = "kanji", cascade = CascadeType.ALL, fetch = LAZY)
    private Set<Word> words = new HashSet<>();
    @ElementCollection(fetch=LAZY)
    private Set<String> meanings = new HashSet<>();
    @JsonProperty(value = "heisig_en")
    private String heisigEn;
    private int jlpt;
    private String unicode;
    @ElementCollection(fetch=LAZY)
    private Set<String> notes = new HashSet<>();

    @Override
    public String toString() {
        return "Kanji{" +
                "id=" + id +
                ", kanji='" + kanji + '\'' +
                ", grade=" + grade +
                ", strokeCount=" + strokeCount +
                ", heisigEn='" + heisigEn + '\'' +
                ", jlpt=" + jlpt +
                ", unicode='" + unicode + '\'' +
                '}';
    }
}
