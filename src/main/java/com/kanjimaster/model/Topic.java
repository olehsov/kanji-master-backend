package com.kanjimaster.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TOPIC")
@Data
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String header;
    private String description;
}
