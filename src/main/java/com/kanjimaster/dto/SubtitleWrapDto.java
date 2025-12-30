package com.kanjimaster.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kanjimaster.model.VideoMetadata;
import com.kanjimaster.model.VideoSubtitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonDeserialize
@JsonSerialize
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubtitleWrapDto {
    @JsonProperty("subtitles")
    private List<SubtitleDto> subtitles;

    @JsonIgnore
    public Set<VideoSubtitle> fromVideoSubtitles() {
        return subtitles.stream().map(SubtitleDto::toVideoSubtitle).collect(Collectors.toSet());
    }

    @JsonIgnore
    public static SubtitleWrapDto toVideoSubtitles(VideoMetadata videoMetadata) {
        List<SubtitleDto> subtitles = videoMetadata.getVideoSubtitles().stream().map(SubtitleDto::fromVideoSubtitle).toList();
        return SubtitleWrapDto.builder().subtitles(subtitles).build();
    }
}
