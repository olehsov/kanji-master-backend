package com.kanjimaster.repository;

import com.kanjimaster.model.VideoMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoMetadataRepository extends JpaRepository<VideoMetadata, Long> {
    Optional<VideoMetadata> findByYoutubeId(String videoId);
}
