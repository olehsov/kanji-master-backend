package com.kanjimaster.repository;

import com.kanjimaster.model.VideoSubtitle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoSubtitlesRepository extends JpaRepository<VideoSubtitle, Long> {
}
