package com.kanjimaster.controller;

import com.kanjimaster.dto.TopicDto;
import com.kanjimaster.service.TopicService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class TopicController {
    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @QueryMapping
    public Mono<List<TopicDto>> getAllTopics() {
        return Mono.just(topicService.findAll());
    }
}
