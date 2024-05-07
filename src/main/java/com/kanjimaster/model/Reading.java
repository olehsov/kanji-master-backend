package com.kanjimaster.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "READING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty(value = "main_kanji")
    @ElementCollection(fetch=LAZY)
    private Set<String> mainKanji = new HashSet<>();
    @JsonProperty(value = "name_kanji")
    @ElementCollection(fetch=LAZY)
    private Set<String> nameKanji = new HashSet<>();
    @Column(unique = true)
    private String reading;

    @ManyToMany(mappedBy = "kunReadings", cascade = CascadeType.ALL, fetch = LAZY)
    private Set<Kanji> kanjisKun = new HashSet<>();
    @ManyToMany(mappedBy = "onReadings", cascade = CascadeType.ALL, fetch = LAZY)
    private Set<Kanji> kanjisOn = new HashSet<>();
    @ManyToMany(mappedBy = "nameReadings", cascade = CascadeType.ALL, fetch = LAZY)
    private Set<Kanji> kanjisName = new HashSet<>();


    @Override
    public String toString() {
        return "Reading{" +
                "id=" + id +
                ", unicode='" + reading + '\'' +
                '}';
    }
}
