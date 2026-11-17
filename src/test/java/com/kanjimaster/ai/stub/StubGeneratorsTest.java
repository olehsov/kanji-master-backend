package com.kanjimaster.ai.stub;

import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.dto.TaskType;
import com.kanjimaster.dto.TopicTaskDto;
import com.kanjimaster.model.Topic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StubGeneratorsTest {

    @Test
    void grammarStubReturnsFullLessonWithRequestedHeader() {
        Topic topic = new Topic();
        topic.setHeader("Passive Voice (受身形)");

        TopicTaskDto dto = new StubGrammarTaskGenerator()
                .generate(topic, List.of(new GrammarSettingPayloadDto(TaskType.FILL_IN_THE_BLANKS, 5)));

        assertThat(dto.getHeader()).isEqualTo("Passive Voice (受身形)");
        assertThat(dto.getFormation()).isNotBlank();
        assertThat(dto.getExampleSentences()).isNotEmpty()
                .allSatisfy(s -> assertThat(s.getSentence()).isNotBlank());
        assertThat(dto.getTasks()).extracting(TopicTaskDto.Task::getTaskType)
                .containsExactlyInAnyOrder(TaskType.values());
    }

    @Test
    void grammarStubIsIndependentOfInput() {
        Topic a = new Topic();
        a.setHeader("A");
        Topic b = new Topic();
        b.setHeader("B");
        StubGrammarTaskGenerator generator = new StubGrammarTaskGenerator();

        assertThat(generator.generate(a, List.of()).getTasks())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(generator.generate(b, List.of()).getTasks());
    }

    @Test
    void subtitleStubReturnsDemoSubtitlesAndIsNotCacheable() {
        StubSubtitleGenerator generator = new StubSubtitleGenerator();

        SubtitleWrapDto dto = generator.generate("qal34e9v_pk");

        assertThat(generator.isCacheable()).isFalse();
        assertThat(dto.getSubtitles()).hasSize(3).allSatisfy(line -> {
            assertThat(line.getLine()).isNotBlank();
            assertThat(line.getTranslation()).isNotBlank();
            assertThat(line.getIndividualTranslations()).isNotEmpty();
        });
    }
}
