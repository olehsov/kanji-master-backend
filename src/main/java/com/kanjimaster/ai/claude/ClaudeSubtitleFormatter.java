package com.kanjimaster.ai.claude;

import com.kanjimaster.ai.AiPrompts;
import com.kanjimaster.ai.AiSchemas;
import com.kanjimaster.ai.SubtitleFormatter;
import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.exception.AiGenerationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

@Component
@ConditionalOnProperty(name = "kanji.ai.provider", havingValue = "claude")
public class ClaudeSubtitleFormatter implements SubtitleFormatter {
    private final ClaudeJsonClient claude;
    private final JsonMapper jsonMapper;
    private final Map<String, Object> schema = AiSchemas.load(AiSchemas.SUBTITLES);

    public ClaudeSubtitleFormatter(ClaudeJsonClient claude, JsonMapper jsonMapper) {
        this.claude = claude;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public SubtitleWrapDto format(String srt) {
        String json = claude.generateJson(AiPrompts.SUBTITLE_SYSTEM, AiPrompts.subtitleUserPrompt(srt), schema);
        try {
            return jsonMapper.readValue(json, SubtitleWrapDto.class);
        } catch (JacksonException e) {
            throw new AiGenerationException("Claude returned malformed subtitle JSON", e);
        }
    }
}
