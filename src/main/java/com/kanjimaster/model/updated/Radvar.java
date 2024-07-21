package com.kanjimaster.model.updated;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "RADVAR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Radvar {
    @Id
    private String radvar;
    private String radical;
    private int number;
    private int strokes;
    private String meaning;
    private String notes;
}
