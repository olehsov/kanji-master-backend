package com.kanjimaster.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanjimaster.model.VideoMetadata;
import com.kanjimaster.model.VideoSubtitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Subtitles of one video, ordered by {@link SubtitleDto#getIndex()}. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubtitleWrapDto {
    private List<SubtitleDto> subtitles;

    @JsonIgnore
    public Set<VideoSubtitle> toVideoSubtitles() {
        return subtitles.stream().map(SubtitleDto::toVideoSubtitle).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static SubtitleWrapDto fromVideoMetadata(VideoMetadata videoMetadata) {
        return new SubtitleWrapDto(videoMetadata.getVideoSubtitles().stream()
                .map(SubtitleDto::fromVideoSubtitle)
                .toList());
    }

    /** Copy with subtitles sorted by index. */
    public SubtitleWrapDto sorted() {
        List<SubtitleDto> list = subtitles == null ? List.of() : subtitles;
        return new SubtitleWrapDto(list.stream().sorted(Comparator.comparingInt(SubtitleDto::getIndex)).toList());
    }
}
