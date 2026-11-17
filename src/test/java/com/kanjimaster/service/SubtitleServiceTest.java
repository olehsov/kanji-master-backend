package com.kanjimaster.service;

import com.kanjimaster.ai.SubtitleGenerator;
import com.kanjimaster.dto.SubtitleDto;
import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubtitleServiceTest {
    private static final String URL = "https://www.youtube.com/watch?v=qal34e9v_pk";
    private static final String ID = "qal34e9v_pk";

    @Mock
    private VideoMetadataService videoMetadataService;
    @Mock
    private SubtitleGenerator generator;
    @InjectMocks
    private SubtitleService service;

    private static SubtitleWrapDto unsorted() {
        return new SubtitleWrapDto(List.of(
                SubtitleDto.builder().index(2).line("b").build(),
                SubtitleDto.builder().index(1).line("a").build()));
    }

    @Test
    void returnsCachedSubtitlesSorted() {
        when(videoMetadataService.findByYoutubeId(ID)).thenReturn(Optional.of(unsorted()));

        SubtitleWrapDto result = service.extract(URL);

        assertThat(result.getSubtitles()).extracting(SubtitleDto::getIndex).containsExactly(1, 2);
        verifyNoInteractions(generator);
    }

    @Test
    void generatesAndStoresWhenProviderIsCacheable() {
        SubtitleWrapDto generated = unsorted();
        when(videoMetadataService.findByYoutubeId(ID)).thenReturn(Optional.empty());
        when(generator.generate(ID)).thenReturn(generated);
        when(generator.isCacheable()).thenReturn(true);

        SubtitleWrapDto result = service.extract(URL);

        verify(videoMetadataService).createYoutubeVideo(generated, ID);
        assertThat(result.getSubtitles()).extracting(SubtitleDto::getIndex).containsExactly(1, 2);
    }

    @Test
    void stubOutputIsNotStored() {
        when(videoMetadataService.findByYoutubeId(ID)).thenReturn(Optional.empty());
        when(generator.generate(ID)).thenReturn(unsorted());
        when(generator.isCacheable()).thenReturn(false);

        service.extract(URL);

        verify(videoMetadataService, never()).createYoutubeVideo(any(), anyString());
    }

    @Test
    void invalidUrlIsRejectedBeforeAnyWork() {
        assertThatThrownBy(() -> service.extract("--exec evil")).isInstanceOf(BadRequestException.class);
        verifyNoInteractions(videoMetadataService, generator);
    }
}
