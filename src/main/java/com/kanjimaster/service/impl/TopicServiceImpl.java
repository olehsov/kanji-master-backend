package com.kanjimaster.service.impl;

import com.kanjimaster.dto.TopicDto;
import com.kanjimaster.exception.NotFoundException;
import com.kanjimaster.model.Topic;
import com.kanjimaster.repository.TopicRepository;
import com.kanjimaster.service.TopicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
        if (id == null) {
            throw new NotFoundException("Topic id must be provided");
        }
        return topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Topic with id " + id + " not found"));
    }
}
