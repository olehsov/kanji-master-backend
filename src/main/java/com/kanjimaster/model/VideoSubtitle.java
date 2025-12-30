package com.kanjimaster.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "VIDEO_SUBTITLE")
@Data
public class VideoSubtitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int index;
    private String timingStart;
    private String timingEnd;
    private String line;
    private String translation;
    @Column(name = "video_id", insertable = false, updatable = false)
    private Long videoId;
    private String kanjies;
    private String grammars;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "video_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private VideoMetadata videoMetadata;

    @OneToMany(mappedBy = "videoSubtitle", fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<IndividualSubtitleTranslation> individualTranslations = new HashSet<>();
}
