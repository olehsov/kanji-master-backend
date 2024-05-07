package com.kanjimaster.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "WORD")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "kanji_id", referencedColumnName = "id")
    private Kanji kanji;
    @ManyToMany(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinTable(
            name = "WORD_MEANING",
            joinColumns = {
                    @JoinColumn(name = "word_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "meaning_id")
            }
    )
    private Set<Meaning> meanings;
    @ManyToMany(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinTable(
            name = "WORD_VARIANT",
            joinColumns = {
                    @JoinColumn(name = "word_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "variant_id")
            }
    )
    private Set<Variant> variants;
}
