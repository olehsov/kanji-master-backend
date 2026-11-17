package com.kanjimaster.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "VIDEO_SUBTITLE")
@Getter
@Setter
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
    private VideoMetadata videoMetadata;

    @OneToMany(mappedBy = "videoSubtitle", fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IndividualSubtitleTranslation> individualTranslations = new HashSet<>();
}
