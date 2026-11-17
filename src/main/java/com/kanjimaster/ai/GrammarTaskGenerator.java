package com.kanjimaster.ai;

import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.dto.TopicTaskDto;
import com.kanjimaster.model.Topic;

import java.util.List;

/** Generates an explanation, examples and exercises for a grammar topic. */
public interface GrammarTaskGenerator {
    TopicTaskDto generate(Topic topic, List<GrammarSettingPayloadDto> settings);
}
