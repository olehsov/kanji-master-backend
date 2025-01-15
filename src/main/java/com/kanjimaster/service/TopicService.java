package com.kanjimaster.service;

import com.kanjimaster.dto.TopicDto;
import com.kanjimaster.model.Topic;

import java.util.List;

public interface TopicService {
    List<TopicDto> findAll();
    Topic findById(Long id);
}
