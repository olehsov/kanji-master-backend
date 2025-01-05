package com.kanjimaster.model.updated;

import jakarta.persistence.*;
import lombok.*;

import java.util.regex.Matcher;

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
    private String word;
    private String reading;
    private String translations;
}
