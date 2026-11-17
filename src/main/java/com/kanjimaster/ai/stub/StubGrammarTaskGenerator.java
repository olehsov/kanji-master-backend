package com.kanjimaster.ai.stub;

import com.kanjimaster.ai.GrammarTaskGenerator;
import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.dto.TopicTaskDto;
import com.kanjimaster.model.Topic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Default provider: always returns the same pre-generated grammar topic, whatever topic or settings were asked
 * for. Only the header follows the requested topic. Keeps the endpoint working without paid AI calls.
 */
@Component
@ConditionalOnProperty(name = "kanji.ai.provider", havingValue = "stub", matchIfMissing = true)
public class StubGrammarTaskGenerator implements GrammarTaskGenerator {

    @Override
    public TopicTaskDto generate(Topic topic, List<GrammarSettingPayloadDto> settings) {
        TopicTaskDto dto = StubResources.read("grammar-task", TopicTaskDto.class);
        dto.setHeader(topic.getHeader());
        return dto;
    }
}
