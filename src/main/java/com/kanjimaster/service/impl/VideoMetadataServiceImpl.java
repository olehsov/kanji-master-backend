package com.kanjimaster.service.impl;

import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.model.IndividualSubtitleTranslation;
import com.kanjimaster.model.VideoMetadata;
import com.kanjimaster.model.VideoSubtitle;
import com.kanjimaster.repository.IndividualTranslationRepository;
import com.kanjimaster.repository.VideoMetadataRepository;
import com.kanjimaster.repository.VideoSubtitlesRepository;
import com.kanjimaster.service.VideoMetadataService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VideoMetadataServiceImpl implements VideoMetadataService {
    private final VideoMetadataRepository videoMetadataRepository;
    private final VideoSubtitlesRepository videoSubtitlesRepository;
    private final IndividualTranslationRepository individualTranslationRepository;

    public VideoMetadataServiceImpl(VideoMetadataRepository videoMetadataRepository, VideoSubtitlesRepository videoSubtitlesRepository, IndividualTranslationRepository individualTranslationRepository) {
        this.videoMetadataRepository = videoMetadataRepository;
        this.videoSubtitlesRepository = videoSubtitlesRepository;
        this.individualTranslationRepository = individualTranslationRepository;
    }

    @Override
    public VideoMetadata createYoutubeVideo(SubtitleWrapDto subtitleWrapDto, String videoId) {
        VideoMetadata videoMetadata = new VideoMetadata();
        videoMetadata.setYoutubeId(videoId);
        videoMetadata.setTitle(Strings.EMPTY);
        videoMetadata.setVideoSubtitles(subtitleWrapDto.fromVideoSubtitles());

        videoMetadata.getVideoSubtitles().forEach(subtitle -> {
            subtitle.setVideoMetadata(videoMetadata);
            subtitle.getIndividualTranslations().forEach(t -> t.setVideoSubtitle(subtitle));
        });

        return videoMetadataRepository.save(videoMetadata);
    }

    @Override
    public SubtitleWrapDto findByYoutubeId(String videoId) {
        Optional<VideoMetadata> videoMetadata = videoMetadataRepository.findByYoutubeId(videoId);
        return videoMetadata.map(SubtitleWrapDto::toVideoSubtitles).orElse(null);
    }


}
