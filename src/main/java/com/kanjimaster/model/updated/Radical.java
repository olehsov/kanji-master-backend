package com.kanjimaster.model.updated;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "RADICAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Radical {
    @Id
    private String radical;
    private String radvar;
    protected String names;
    private int number;
    private int strokes;
    private String meaning;
    private String notes;
}
