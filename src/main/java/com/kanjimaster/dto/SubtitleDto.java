package com.kanjimaster.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kanjimaster.model.IndividualSubtitleTranslation;
import com.kanjimaster.model.VideoSubtitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonDeserialize
@JsonSerialize
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubtitleDto {
    @JsonProperty("index")
    private int index;

    @JsonProperty("timingStart")
    private String timingStart;

    @JsonProperty("timingEnd")
    private String timingEnd;

    @JsonProperty("line")
    private String line;

    @JsonProperty("translation")
    private String translation;

    @JsonProperty("kanjies")
    private String kanjies;

    @JsonProperty("grammars")
    private String grammars;

    @JsonProperty("individualTranslations")
    private List<IndividualTranslationDto> individualTranslations;


    @JsonIgnore
    public VideoSubtitle toVideoSubtitle() {
        VideoSubtitle videoSubtitle = new VideoSubtitle();
        videoSubtitle.setIndex(index);
        videoSubtitle.setTimingStart(timingStart);
        videoSubtitle.setTimingEnd(timingEnd);
        videoSubtitle.setLine(line);
        videoSubtitle.setTranslation(translation);
        videoSubtitle.setKanjies(kanjies);
        videoSubtitle.setGrammars(grammars);

        Set<IndividualSubtitleTranslation> individualSubtitleTranslation = individualTranslations.stream()
                .map(IndividualTranslationDto::toIndividualSubtitleTranslation)
                .collect(Collectors.toSet());
        videoSubtitle.setIndividualTranslations(individualSubtitleTranslation);
        return videoSubtitle;
    }

    @JsonIgnore
    public static SubtitleDto fromVideoSubtitle(VideoSubtitle videoSubtitle) {

        List<IndividualTranslationDto> translations = videoSubtitle.getIndividualTranslations().stream()
                .map(IndividualTranslationDto::fromIndividualSubtitleTranslation)
                .toList();

        return SubtitleDto.builder()
                .index(videoSubtitle.getIndex())
                .timingStart(videoSubtitle.getTimingStart())
                .timingEnd(videoSubtitle.getTimingEnd())
                .line(videoSubtitle.getLine())
                .translation(videoSubtitle.getTranslation())
                .kanjies(videoSubtitle.getKanjies())
                .grammars(videoSubtitle.getGrammars())
                .individualTranslations(translations)
                .build();
    }
}
