package com.kanjimaster.ai.claude;

import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.dto.TaskType;
import com.kanjimaster.dto.TopicTaskDto;
import com.kanjimaster.exception.AiGenerationException;
import com.kanjimaster.model.Topic;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClaudeGeneratorsTest {
    private final ClaudeJsonClient claude = mock(ClaudeJsonClient.class);
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Test
    void grammarGeneratorParsesStructuredOutputAndSendsSettings() {
        Topic topic = new Topic();
        topic.setHeader("Te-Form (て形)");
        when(claude.generateJson(anyString(), argThat(prompt -> prompt.contains("Te-Form")
                && prompt.contains("3 tasks of type FILL_IN_THE_BLANKS")), anyMap()))
                .thenReturn("""
                        {"formation":"<b>て</b>","exampleSentences":[{"sentence":"食べて","translation":"їж"}],
                         "tasks":[{"taskType":"FILL_IN_THE_BLANKS","description":"d","question":"q",
                                   "correctAnswer":"a","resources":[]}]}""");

        TopicTaskDto dto = new ClaudeGrammarTaskGenerator(claude, jsonMapper)
                .generate(topic, List.of(new GrammarSettingPayloadDto(TaskType.FILL_IN_THE_BLANKS, 3)));

        assertThat(dto.getHeader()).isEqualTo("Te-Form (て形)");
        assertThat(dto.getTasks()).singleElement()
                .extracting(TopicTaskDto.Task::getTaskType).isEqualTo(TaskType.FILL_IN_THE_BLANKS);
    }

    @Test
    void malformedGrammarJsonBecomesAiError() {
        Topic topic = new Topic();
        topic.setHeader("x");
        when(claude.generateJson(anyString(), anyString(), anyMap())).thenReturn("not json");

        assertThatThrownBy(() -> new ClaudeGrammarTaskGenerator(claude, jsonMapper).generate(topic, List.of()))
                .isInstanceOf(AiGenerationException.class);
    }

    @Test
    void subtitleFormatterSendsSrtAndParsesResult() {
        when(claude.generateJson(anyString(), argThat(p -> p.contains("00:00:01,000")), anyMap()))
                .thenReturn("""
                        {"subtitles":[{"index":1,"timingStart":"00:00:01,000","timingEnd":"00:00:02,000",
                          "line":"猫","translation":"кіт","kanjies":"猫","grammars":"",
                          "individualTranslations":[{"text":"猫","textHiragana":"ねこ","meaning":"кіт",
                                                     "isWord":true,"jlpt":"N4"}]}]}""");

        SubtitleWrapDto dto = new ClaudeSubtitleFormatter(claude, jsonMapper)
                .format("1\n00:00:01,000 --> 00:00:02,000\n猫\n");

        assertThat(dto.getSubtitles()).singleElement().satisfies(line -> {
            assertThat(line.getTranslation()).isEqualTo("кіт");
            assertThat(line.getIndividualTranslations()).singleElement()
                    .satisfies(t -> assertThat(t.isWord()).isTrue());
        });
    }

    @Test
    void schemaIsPassedToClaude() {
        when(claude.generateJson(anyString(), anyString(), argThat(schema -> schema.containsKey("properties")
                && ((java.util.Map<?, ?>) schema.get("properties")).containsKey("subtitles"))))
                .thenReturn("{\"subtitles\":[]}");

        assertThat(new ClaudeSubtitleFormatter(claude, jsonMapper).format("srt").getSubtitles()).isEmpty();
    }
}
