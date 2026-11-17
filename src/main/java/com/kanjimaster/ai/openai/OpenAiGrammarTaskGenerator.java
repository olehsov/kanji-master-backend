package com.kanjimaster.ai.openai;

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

/**
 * Legacy grammar generation with OpenAI chat.
 *
 * @deprecated replaced by {@link com.kanjimaster.ai.claude.ClaudeGrammarTaskGenerator}; kept until Claude is live.
 */
@Deprecated
@Component
@ConditionalOnProperty(name = "kanji.ai.provider", havingValue = "openai")
public class OpenAiGrammarTaskGenerator implements GrammarTaskGenerator {
    private final OpenAiHttpClient client;
    private final JsonMapper jsonMapper;
    private final Map<String, Object> schema = AiSchemas.load(AiSchemas.GRAMMAR_TASK);

    public OpenAiGrammarTaskGenerator(OpenAiHttpClient client, JsonMapper jsonMapper) {
        this.client = client;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public TopicTaskDto generate(Topic topic, List<GrammarSettingPayloadDto> settings) {
        String json = client.chatJson("JapaneseTaskBuilder", schema, AiPrompts.GRAMMAR_SYSTEM,
                AiPrompts.grammarUserPrompt(topic, settings));
        try {
            TopicTaskDto dto = jsonMapper.readValue(json, TopicTaskDto.class);
            dto.setHeader(topic.getHeader());
            return dto;
        } catch (JacksonException e) {
            throw new AiGenerationException("OpenAI returned malformed grammar JSON", e);
        }
    }
}
