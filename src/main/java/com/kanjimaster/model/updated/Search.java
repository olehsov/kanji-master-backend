package com.kanjimaster.model.updated;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "search")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Search {
    @Id
    private String kanji;
    private int grade;
    private String regReading;
    private String reading;
    private String meaning;
}
