package com.kanjimaster.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "VIDEO_METADATA")
@Getter
@Setter
public class VideoMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "youtube_id", nullable = false)
    private String youtubeId;

    @OneToMany(mappedBy = "videoMetadata", fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoSubtitle> videoSubtitles = new HashSet<>();
}
