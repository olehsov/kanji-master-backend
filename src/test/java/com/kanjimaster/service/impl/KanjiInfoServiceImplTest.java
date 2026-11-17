package com.kanjimaster.service.impl;

import com.kanjimaster.dto.KanjiFilterDto;
import com.kanjimaster.dto.KanjiInfoDto;
import com.kanjimaster.exception.BadRequestException;
import com.kanjimaster.exception.NotFoundException;
import com.kanjimaster.model.updated.KanjiInfo;
import com.kanjimaster.model.updated.Word;
import com.kanjimaster.repository.KanjiInfoRepository;
import com.kanjimaster.repository.SearchRepository;
import com.kanjimaster.support.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KanjiInfoServiceImplTest {
    @Mock
    private KanjiInfoRepository kanjiInfoRepository;
    @Mock
    private SearchRepository searchRepository;
    @InjectMocks
    private KanjiInfoServiceImpl service;

    @Test
    void getKanjiReturnsDtoWithAtMost15Words() {
        KanjiInfo kanji = TestData.kanji("日", 1);
        List<Word> words = IntStream.range(0, 20).mapToObj(i -> TestData.word(i, "日" + i, 100 - i)).toList();
        kanji.setWords(words);
        when(kanjiInfoRepository.findById("日")).thenReturn(Optional.of(kanji));

        KanjiInfoDto dto = service.getKanji("日");

        assertThat(dto.getKanji()).isEqualTo("日");
        assertThat(dto.getWords()).hasSize(15).first().extracting(Word::getWord).isEqualTo("日0");
    }

    @Test
    void getKanjiThrowsNotFoundForUnknownKanji() {
        when(kanjiInfoRepository.findById("無")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getKanji("無")).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("無");
    }

    @Test
    void getKanjiRejectsBlankInput() {
        assertThatThrownBy(() -> service.getKanji(" ")).isInstanceOf(BadRequestException.class);
        verifyNoInteractions(kanjiInfoRepository);
    }

    @Test
    void getKanjiPageWithoutFilterReturnsEverything() {
        Page<KanjiInfo> page = new PageImpl<>(List.of(TestData.kanji("日", 1)));
        when(kanjiInfoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        assertThat(service.getKanjiPage(0, 20, null)).isSameAs(page);
    }

    @Test
    void searchThatMatchesNothingReturnsEmptyPage() {
        when(searchRepository.findAllKanjiBySearch("zzz")).thenReturn(List.of());

        Page<KanjiInfo> page = service.getKanjiPage(0, 20, KanjiFilterDto.builder().search("zzz").build());

        assertThat(page.getContent()).isEmpty();
        verify(kanjiInfoRepository, never()).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void searchTermIsTrimmedAndUsed() {
        when(searchRepository.findAllKanjiBySearch("nichi")).thenReturn(List.of("日"));
        Page<KanjiInfo> page = new PageImpl<>(List.of(TestData.kanji("日", 1)));
        when(kanjiInfoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        assertThat(service.getKanjiPage(0, 20, KanjiFilterDto.builder().search(" nichi ").build())).isSameAs(page);
    }

    @Test
    void kanjiTypedDirectlyMatchesItself() {
        when(searchRepository.findAllKanjiBySearch("人")).thenReturn(List.of());
        Page<KanjiInfo> page = new PageImpl<>(List.of(TestData.kanji("人", 5)));
        when(kanjiInfoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        assertThat(service.getKanjiPage(0, 20, KanjiFilterDto.builder().search("人").build())).isSameAs(page);
    }

    @Test
    void extractsOnlyKanjiCharacters() {
        assertThat(KanjiInfoServiceImpl.kanjiCharacters("日本の人 hito 日")).containsExactly("日", "本", "人");
        assertThat(KanjiInfoServiceImpl.kanjiCharacters("ひと hito")).isEmpty();
    }

    @Test
    void invalidPagingIsRejected() {
        assertThatThrownBy(() -> service.getKanjiPage(-1, 20, null)).isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> service.getKanjiPage(0, 0, null)).isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> service.getKanjiPage(0, 101, null)).isInstanceOf(BadRequestException.class);
    }

    @Test
    void wordRepresentationDefaultsToEmptyForKanjiWithoutWords() {
        KanjiInfo sun = TestData.kanji("日", 1);
        KanjiInfo rare = TestData.kanji("鬱", 2000);
        when(kanjiInfoRepository.findAll(any(Specification.class))).thenReturn(List.of(sun, rare));
        when(kanjiInfoRepository.findKanjiWordRepresentations(List.of("日", "鬱")))
                .thenReturn(List.<Object[]>of(new Object[]{"日", "日本(nihon)"}));

        Map<KanjiInfo, String> result = service.getKanjiesByFilterWithWordRep(new KanjiFilterDto());

        assertThat(result).containsEntry(sun, "日本(nihon)").containsEntry(rare, "");
    }

    @Test
    void wordRepresentationSkipsQueryWhenNothingMatches() {
        when(searchRepository.findAllKanjiBySearch("zzz")).thenReturn(List.of());

        assertThat(service.getKanjiesByFilterWithWordRep(KanjiFilterDto.builder().search("zzz").build())).isEmpty();
        verify(kanjiInfoRepository, never()).findKanjiWordRepresentations(anyList());
    }

    @Test
    void kanjiByRadicalWithNoRadicalsReturnsEmpty() {
        assertThat(service.getKanjiesByRadical(Set.of())).isEmpty();
        assertThat(service.getKanjiesByRadical(null)).isEmpty();
        verifyNoInteractions(kanjiInfoRepository);
    }
}
