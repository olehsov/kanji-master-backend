package com.kanjimaster.controller;

import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.dto.TopicDto;
import com.kanjimaster.dto.TopicTaskDto;
import com.kanjimaster.service.GrammarTaskService;
import com.kanjimaster.service.TopicService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TopicController {
    private final TopicService topicService;
    private final GrammarTaskService grammarTaskService;

    public TopicController(TopicService topicService, GrammarTaskService grammarTaskService) {
        this.topicService = topicService;
        this.grammarTaskService = grammarTaskService;
    }

    @QueryMapping
    public List<TopicDto> getAllTopics() {
        return topicService.findAll();
    }

    @QueryMapping
    public TopicTaskDto getTopicTask(@Argument Long topicId, @Argument List<GrammarSettingPayloadDto> settings) {
        return grammarTaskService.generate(topicId, settings);
    }
}
