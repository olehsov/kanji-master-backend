package com.kanjimaster.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "KANJI")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Kanji {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kanji;
    private int grade;
    @JsonProperty(value = "stroke_count")
    private Integer strokeCount;
    @ElementCollection(fetch=EAGER)
    private Set<String> meanings = new HashSet<>();
    @JsonProperty(value = "heisig_en")
    private String heisigEn;
    @JsonProperty(value = "kun_readings")
    @ElementCollection(fetch=EAGER)
    private Set<String> kunReadings = new HashSet<>();
    @JsonProperty(value = "on_readings")
    @ElementCollection(fetch=EAGER)
    private Set<String> onReadings = new HashSet<>();
    @JsonProperty(value = "name_readings")
    @ElementCollection(fetch=LAZY)
    private Set<String> nameReadings = new HashSet<>();
    private int jlpt;
    private String unicode;
    @ElementCollection(fetch=EAGER)
    private Set<String> notes = new HashSet<>();
}
