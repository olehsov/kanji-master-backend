package com.kanjimaster.service.impl;

import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.model.VideoMetadata;
import com.kanjimaster.repository.VideoMetadataRepository;
import com.kanjimaster.service.VideoMetadataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class VideoMetadataServiceImpl implements VideoMetadataService {
    private final VideoMetadataRepository videoMetadataRepository;

    public VideoMetadataServiceImpl(VideoMetadataRepository videoMetadataRepository) {
        this.videoMetadataRepository = videoMetadataRepository;
    }

    @Override
    @Transactional
    public VideoMetadata createYoutubeVideo(SubtitleWrapDto subtitleWrapDto, String videoId) {
        VideoMetadata videoMetadata = new VideoMetadata();
        videoMetadata.setYoutubeId(videoId);
        videoMetadata.setTitle("");
        videoMetadata.setVideoSubtitles(subtitleWrapDto.toVideoSubtitles());

        videoMetadata.getVideoSubtitles().forEach(subtitle -> {
            subtitle.setVideoMetadata(videoMetadata);
            subtitle.getIndividualTranslations().forEach(t -> t.setVideoSubtitle(subtitle));
        });

        return videoMetadataRepository.save(videoMetadata);
    }

    @Override
    public Optional<SubtitleWrapDto> findByYoutubeId(String videoId) {
        return videoMetadataRepository.findByYoutubeId(videoId).map(SubtitleWrapDto::fromVideoMetadata);
    }
}
