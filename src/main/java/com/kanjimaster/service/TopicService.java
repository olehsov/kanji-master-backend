package com.kanjimaster.service;

import com.kanjimaster.dto.TopicDto;

import java.util.List;

public interface TopicService {
    List<TopicDto> findAll();
}
