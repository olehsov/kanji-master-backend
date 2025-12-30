package com.kanjimaster.service;

import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.model.VideoMetadata;

public interface VideoMetadataService {
    VideoMetadata createYoutubeVideo(SubtitleWrapDto subtitleWrapDto, String videoId);
    SubtitleWrapDto findByYoutubeId(String videoId);
}
