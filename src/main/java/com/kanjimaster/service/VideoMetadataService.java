package com.kanjimaster.service;

import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.model.VideoMetadata;

import java.util.Optional;

public interface VideoMetadataService {
    VideoMetadata createYoutubeVideo(SubtitleWrapDto subtitleWrapDto, String videoId);

    Optional<SubtitleWrapDto> findByYoutubeId(String videoId);
}
