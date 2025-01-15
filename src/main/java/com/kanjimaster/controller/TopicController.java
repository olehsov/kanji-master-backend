package com.kanjimaster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kanjimaster.client.OpenAiClientService;
import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.dto.TopicDto;
import com.kanjimaster.dto.TopicTaskDto;
import com.kanjimaster.model.Topic;
import com.kanjimaster.service.TopicService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class TopicController {
    private final TopicService topicService;
    private final OpenAiClientService openAiClientService;

    public TopicController(TopicService topicService, OpenAiClientService openAiClientService) {
        this.topicService = topicService;
        this.openAiClientService = openAiClientService;
    }

    @QueryMapping
    public Mono<List<TopicDto>> getAllTopics() {
        return Mono.just(topicService.findAll());
    }

    @QueryMapping
    public Mono<TopicTaskDto> getTopicTask(@Argument Long topicId, @Argument List<GrammarSettingPayloadDto> settings) throws JsonProcessingException {
        Topic topic = topicService.findById(topicId);
        return Mono.just(openAiClientService.getTopicTasks(topic, settings));
    }
}
