package com.kanjimaster.service.impl;

import com.kanjimaster.dto.SubtitleDto;
import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.dto.TopicDto;
import com.kanjimaster.exception.BadRequestException;
import com.kanjimaster.exception.NotFoundException;
import com.kanjimaster.model.VideoMetadata;
import com.kanjimaster.repository.SentenceRepository;
import com.kanjimaster.repository.TopicRepository;
import com.kanjimaster.repository.VideoMetadataRepository;
import com.kanjimaster.support.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleServicesTest {

    @Test
    void topicServiceMapsAndFinds() {
        TopicRepository repository = mock(TopicRepository.class);
        TopicServiceImpl service = new TopicServiceImpl(repository);
        when(repository.findAll()).thenReturn(List.of(TestData.topic(1L, "Te-Form")));
        when(repository.findById(1L)).thenReturn(Optional.of(TestData.topic(1L, "Te-Form")));
        when(repository.findById(9L)).thenReturn(Optional.empty());

        assertThat(service.findAll()).extracting(TopicDto::getHeader).containsExactly("Te-Form");
        assertThat(service.findById(1L).getHeader()).isEqualTo("Te-Form");
        assertThatThrownBy(() -> service.findById(9L)).isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> service.findById(null)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void sentenceServiceValidatesInput() {
        SentenceRepository repository = mock(SentenceRepository.class);
        SentenceServiceImpl service = new SentenceServiceImpl(repository);
        when(repository.findAllByWordContains(PageRequest.of(0, 5), "日")).thenReturn(new PageImpl<>(List.of()));

        assertThat(service.findSentencesByKanji(0, 5, "日")).isEmpty();
        assertThatThrownBy(() -> service.findSentencesByKanji(0, 5, "")).isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> service.findSentencesByKanji(0, 500, "日")).isInstanceOf(BadRequestException.class);
    }

    @Test
    void videoMetadataServiceLinksChildrenBeforeSaving() {
        VideoMetadataRepository repository = mock(VideoMetadataRepository.class);
        VideoMetadataServiceImpl service = new VideoMetadataServiceImpl(repository);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        SubtitleWrapDto dto = new SubtitleWrapDto(List.of(SubtitleDto.builder().index(1).line("猫")
                .individualTranslations(List.of(com.kanjimaster.dto.IndividualTranslationDto.builder().text("猫").build()))
                .build()));

        VideoMetadata saved = service.createYoutubeVideo(dto, "qal34e9v_pk");

        assertThat(saved.getYoutubeId()).isEqualTo("qal34e9v_pk");
        assertThat(saved.getVideoSubtitles()).singleElement().satisfies(subtitle -> {
            assertThat(subtitle.getVideoMetadata()).isSameAs(saved);
            assertThat(subtitle.getIndividualTranslations()).singleElement()
                    .satisfies(t -> assertThat(t.getVideoSubtitle()).isSameAs(subtitle));
        });
    }

    @Test
    void videoMetadataServiceFindsByYoutubeId() {
        VideoMetadataRepository repository = mock(VideoMetadataRepository.class);
        VideoMetadataServiceImpl service = new VideoMetadataServiceImpl(repository);
        when(repository.findByYoutubeId("missing")).thenReturn(Optional.empty());

        assertThat(service.findByYoutubeId("missing")).isEmpty();
    }
}
