package com.kanjimaster.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "VIDEO_METADATA")
@Data
public class VideoMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "youtube_id", nullable = false)
    private String youtubeId;

    @OneToMany(mappedBy = "videoMetadata", fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<VideoSubtitle> videoSubtitles = new HashSet<>();
}
