package com.kanjimaster.service;

import com.kanjimaster.ai.GrammarTaskGenerator;
import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.dto.TaskType;
import com.kanjimaster.dto.TopicTaskDto;
import com.kanjimaster.exception.BadRequestException;
import com.kanjimaster.model.Topic;
import com.kanjimaster.support.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrammarTaskServiceTest {
    @Mock
    private TopicService topicService;
    @Mock
    private GrammarTaskGenerator generator;
    @InjectMocks
    private GrammarTaskService service;

    @Test
    void generatesForValidSettings() {
        Topic topic = TestData.topic(2L, "Te-Form");
        List<GrammarSettingPayloadDto> settings = List.of(
                new GrammarSettingPayloadDto(TaskType.FILL_IN_THE_BLANKS, 20),
                new GrammarSettingPayloadDto(TaskType.TRANSLATE_TO_JAPANESE, 0));
        TopicTaskDto expected = new TopicTaskDto();
        when(topicService.findById(2L)).thenReturn(topic);
        when(generator.generate(topic, settings)).thenReturn(expected);

        assertThat(service.generate(2L, settings)).isSameAs(expected);
    }

    @Test
    void rejectsMissingSettings() {
        assertThatThrownBy(() -> service.generate(1L, null)).isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> service.generate(1L, List.of())).isInstanceOf(BadRequestException.class);
        verifyNoInteractions(generator);
    }

    @Test
    void rejectsTooManyTasks() {
        List<GrammarSettingPayloadDto> settings = List.of(new GrammarSettingPayloadDto(TaskType.FILL_IN_THE_BLANKS, 21));
        assertThatThrownBy(() -> service.generate(1L, settings)).isInstanceOf(BadRequestException.class)
                .hasMessageContaining("20");
        verifyNoInteractions(generator, topicService);
    }

    @Test
    void rejectsNegativeAmountMissingTypeAndDuplicates() {
        assertThatThrownBy(() -> service.generate(1L, List.of(new GrammarSettingPayloadDto(TaskType.FILL_IN_THE_BLANKS, -1))))
                .isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> service.generate(1L, List.of(new GrammarSettingPayloadDto(null, 1))))
                .isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> service.generate(1L, List.of(
                new GrammarSettingPayloadDto(TaskType.FILL_IN_THE_BLANKS, 1),
                new GrammarSettingPayloadDto(TaskType.FILL_IN_THE_BLANKS, 2))))
                .isInstanceOf(BadRequestException.class).hasMessageContaining("once");
    }

    @Test
    void ignoresNullEntries() {
        Topic topic = TestData.topic(1L, "x");
        GrammarSettingPayloadDto setting = new GrammarSettingPayloadDto(TaskType.CONVERT_TO_FORMATION, 3);
        when(topicService.findById(1L)).thenReturn(topic);
        when(generator.generate(topic, List.of(setting))).thenReturn(new TopicTaskDto());

        assertThat(service.generate(1L, Arrays.asList(setting, null))).isNotNull();
    }
}
