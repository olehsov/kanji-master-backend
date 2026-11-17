package com.kanjimaster.service;

import com.kanjimaster.ai.GrammarTaskGenerator;
import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.dto.TopicTaskDto;
import com.kanjimaster.exception.BadRequestException;
import com.kanjimaster.model.Topic;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/** Validates grammar-task requests and delegates to the active AI provider. */
@Service
public class GrammarTaskService {
    public static final int MAX_TASKS_PER_TYPE = 20;

    private final TopicService topicService;
    private final GrammarTaskGenerator generator;

    public GrammarTaskService(TopicService topicService, GrammarTaskGenerator generator) {
        this.topicService = topicService;
        this.generator = generator;
    }

    public TopicTaskDto generate(Long topicId, List<GrammarSettingPayloadDto> settings) {
        List<GrammarSettingPayloadDto> validSettings = validate(settings);
        Topic topic = topicService.findById(topicId);
        return generator.generate(topic, validSettings);
    }

    private static List<GrammarSettingPayloadDto> validate(List<GrammarSettingPayloadDto> settings) {
        if (settings == null) {
            return List.of();
        }
        List<GrammarSettingPayloadDto> nonNull = settings.stream().filter(Objects::nonNull).toList();
        for (GrammarSettingPayloadDto setting : nonNull) {
            if (setting.getType() == null) {
                throw new BadRequestException("Task type is required");
            }
            if (setting.getTaskAmount() < 0 || setting.getTaskAmount() > MAX_TASKS_PER_TYPE) {
                throw new BadRequestException("taskAmount must be between 0 and " + MAX_TASKS_PER_TYPE);
            }
        }
        if (nonNull.stream().map(GrammarSettingPayloadDto::getType).distinct().count() != nonNull.size()) {
            throw new BadRequestException("Each task type may appear only once");
        }
        return nonNull;
    }
}
