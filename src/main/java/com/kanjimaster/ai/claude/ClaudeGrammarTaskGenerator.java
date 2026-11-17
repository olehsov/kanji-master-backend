package com.kanjimaster.ai.claude;

import com.kanjimaster.ai.AiPrompts;
import com.kanjimaster.ai.AiSchemas;
import com.kanjimaster.ai.GrammarTaskGenerator;
import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.dto.TopicTaskDto;
import com.kanjimaster.exception.AiGenerationException;
import com.kanjimaster.model.Topic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "kanji.ai.provider", havingValue = "claude")
public class ClaudeGrammarTaskGenerator implements GrammarTaskGenerator {
    private final ClaudeJsonClient claude;
    private final JsonMapper jsonMapper;
    private final Map<String, Object> schema = AiSchemas.load(AiSchemas.GRAMMAR_TASK);

    public ClaudeGrammarTaskGenerator(ClaudeJsonClient claude, JsonMapper jsonMapper) {
        this.claude = claude;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public TopicTaskDto generate(Topic topic, List<GrammarSettingPayloadDto> settings) {
        String json = claude.generateJson(AiPrompts.GRAMMAR_SYSTEM, AiPrompts.grammarUserPrompt(topic, settings), schema);
        try {
            TopicTaskDto dto = jsonMapper.readValue(json, TopicTaskDto.class);
            dto.setHeader(topic.getHeader());
            return dto;
        } catch (JacksonException e) {
            throw new AiGenerationException("Claude returned malformed grammar JSON", e);
        }
    }
}
