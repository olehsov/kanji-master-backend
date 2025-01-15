package com.kanjimaster.service.impl;

import com.kanjimaster.dto.TopicDto;
import com.kanjimaster.model.Topic;
import com.kanjimaster.repository.TopicRepository;
import com.kanjimaster.service.TopicService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public List<TopicDto> findAll() {
        return topicRepository.findAll().stream().map(TopicDto::fromTopic).toList();
    }

    @Override
    public Topic findById(Long id) {
        return topicRepository.findById(id).orElseThrow(() -> new RuntimeException("No topic with id " + id + " found"));
    }
}
