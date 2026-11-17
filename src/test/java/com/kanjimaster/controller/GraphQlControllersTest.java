package com.kanjimaster.controller;

import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.dto.KanjiFilterDto;
import com.kanjimaster.dto.KanjiInfoDto;
import com.kanjimaster.dto.RadicalSearchOptionDto;
import com.kanjimaster.dto.TaskType;
import com.kanjimaster.dto.TopicDto;
import com.kanjimaster.dto.TopicTaskDto;
import com.kanjimaster.exception.BadRequestException;
import com.kanjimaster.exception.GraphQlExceptionResolver;
import com.kanjimaster.exception.NotFoundException;
import com.kanjimaster.model.updated.Sentence;
import com.kanjimaster.service.GrammarTaskService;
import com.kanjimaster.service.KanjiInfoService;
import com.kanjimaster.service.RadicalSearchService;
import com.kanjimaster.service.SentenceService;
import com.kanjimaster.service.TopicService;
import com.kanjimaster.support.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@GraphQlTest({KanjiController.class, SentenceController.class, RadicalSearchController.class, TopicController.class})
@Import(GraphQlExceptionResolver.class)
class GraphQlControllersTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private KanjiInfoService kanjiInfoService;
    @MockitoBean
    private SentenceService sentenceService;
    @MockitoBean
    private RadicalSearchService radicalSearchService;
    @MockitoBean
    private TopicService topicService;
    @MockitoBean
    private GrammarTaskService grammarTaskService;

    @Test
    void getKanjisUsesDefaultsAndMapsPage() {
        when(kanjiInfoService.getKanjiPage(eq(0), eq(20), isNull()))
                .thenReturn(new PageImpl<>(List.of(TestData.kanji("日", 1)), PageRequest.of(0, 20), 1));

        graphQlTester.document("{ getKanjis { content { kanji } number first last totalElements } }")
                .execute()
                .path("getKanjis.content[0].kanji").entity(String.class).isEqualTo("日")
                .path("getKanjis.first").entity(Boolean.class).isEqualTo(true)
                .path("getKanjis.last").entity(Boolean.class).isEqualTo(true)
                .path("getKanjis.totalElements").entity(Integer.class).isEqualTo(1);
    }

    @Test
    void getKanjisPassesFilter() {
        when(kanjiInfoService.getKanjiPage(eq(1), eq(10), argThat((KanjiFilterDto f) ->
                "nichi".equals(f.getSearch()) && f.getJlpt().equals(List.of("5")))))
                .thenReturn(new PageImpl<>(List.of()));

        graphQlTester.document("""
                        { getKanjis(page: 1, size: 10, filter: {search: "nichi", jlpt: ["5"]}) { content { kanji } } }""")
                .execute()
                .path("getKanjis.content").entityList(Object.class).hasSize(0);
    }

    @Test
    void invalidPagingIsBadRequest() {
        when(kanjiInfoService.getKanjiPage(eq(0), eq(1000), any())).thenThrow(new BadRequestException("size must be between 1 and 100"));

        graphQlTester.document("{ getKanjis(size: 1000) { number } }")
                .execute()
                .errors().satisfy(errors -> assertThat(errors).singleElement()
                        .satisfies(e -> assertThat(e.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)));
    }

    @Test
    void getKanjiReturnsDetailsWithWords() {
        var kanji = TestData.kanji("日", 1);
        kanji.setWords(List.of(TestData.word(1, "日本", 100)));
        when(kanjiInfoService.getKanji("日")).thenReturn(KanjiInfoDto.fromKanjiInfo(kanji));

        graphQlTester.document("{ getKanji(kanji: \"日\") { kanji meaning words { word translation } } }")
                .execute()
                .path("getKanji.meaning").entity(String.class).isEqualTo("day;sun")
                .path("getKanji.words[0].word").entity(String.class).isEqualTo("日本")
                .path("getKanji.words[0].translation").entity(String.class).isEqualTo("Японія");
    }

    @Test
    void unknownKanjiIsNotFound() {
        when(kanjiInfoService.getKanji("無")).thenThrow(new NotFoundException("Kanji '無' not found"));

        graphQlTester.document("{ getKanji(kanji: \"無\") { kanji } }")
                .execute()
                .errors().satisfy(errors -> assertThat(errors).singleElement().satisfies(e -> {
                    assertThat(e.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
                    assertThat(e.getMessage()).contains("無");
                }));
    }

    @Test
    void getKanjiesByRadical() {
        when(kanjiInfoService.getKanjiesByRadical(Set.of("日", "月"))).thenReturn(List.of(TestData.kanji("明", 5)));

        graphQlTester.document("{ getKanjiesByRadical(radicals: [\"日\", \"月\"]) { kanji } }")
                .execute()
                .path("getKanjiesByRadical[0].kanji").entity(String.class).isEqualTo("明");
    }

    @Test
    void radicalOptionsExposeIsSimplifiedRadical() {
        when(radicalSearchService.findAllRadicalsSearchOptions()).thenReturn(List.of(
                RadicalSearchOptionDto.builder().radical("氵").strokes(3).isSimplifiedRadical(true).build()));

        graphQlTester.document("{ getRadicalSearchOptionDto { radical strokes isSimplifiedRadical } }")
                .execute()
                .path("getRadicalSearchOptionDto[0].radical").entity(String.class).isEqualTo("氵")
                .path("getRadicalSearchOptionDto[0].isSimplifiedRadical").entity(Boolean.class).isEqualTo(true);
    }

    @Test
    void getSentencesByKanji() {
        Sentence sentence = Sentence.builder().id(7L).japanese("日本へ行く").english("go to Japan").word("日本")
                .kanji("日").particle("へ").furigana("にほん").obi2("").build();
        when(sentenceService.findSentencesByKanji(0, 5, "日")).thenReturn(new PageImpl<>(List.of(sentence)));

        graphQlTester.document("{ getSentencesByKanji(page: 0, size: 5, kanji: \"日\") { content { id japanese } } }")
                .execute()
                .path("getSentencesByKanji.content[0].id").entity(Integer.class).isEqualTo(7)
                .path("getSentencesByKanji.content[0].japanese").entity(String.class).isEqualTo("日本へ行く");
    }

    @Test
    void getAllTopics() {
        when(topicService.findAll()).thenReturn(List.of(TopicDto.fromTopic(TestData.topic(1L, "Te-Form"))));

        graphQlTester.document("{ getAllTopics { id header description } }")
                .execute()
                .path("getAllTopics[0].id").entity(String.class).isEqualTo("1")
                .path("getAllTopics[0].header").entity(String.class).isEqualTo("Te-Form");
    }

    @Test
    void getTopicTaskBindsEnumSettings() {
        TopicTaskDto dto = new TopicTaskDto();
        dto.setHeader("Te-Form");
        dto.setFormation("f");
        dto.setExampleSentences(List.of());
        TopicTaskDto.Task task = new TopicTaskDto.Task();
        task.setTaskType(TaskType.FILL_IN_THE_BLANKS);
        task.setDescription("d");
        task.setQuestion("q");
        task.setCorrectAnswer("a");
        task.setResources(List.of());
        dto.setTasks(List.of(task));
        when(grammarTaskService.generate(eq(3L), argThat((List<GrammarSettingPayloadDto> s) ->
                s.size() == 1 && s.getFirst().getType() == TaskType.FILL_IN_THE_BLANKS && s.getFirst().getTaskAmount() == 2)))
                .thenReturn(dto);

        // Variables arrive as plain strings from the frontend and must coerce to the TaskType enum.
        graphQlTester.document("""
                        query($topicId: Int, $settings: [GrammarSettingPayload]) {
                          getTopicTask(topicId: $topicId, settings: $settings) { header tasks { taskType question } }
                        }""")
                .variable("topicId", 3)
                .variable("settings", List.of(java.util.Map.of("type", "FILL_IN_THE_BLANKS", "taskAmount", 2)))
                .execute()
                .path("getTopicTask.header").entity(String.class).isEqualTo("Te-Form")
                .path("getTopicTask.tasks[0].taskType").entity(String.class).isEqualTo("FILL_IN_THE_BLANKS");
    }
}
