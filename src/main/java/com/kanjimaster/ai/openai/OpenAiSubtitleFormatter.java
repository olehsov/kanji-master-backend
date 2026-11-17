package com.kanjimaster.ai.openai;

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

/**
 * Legacy subtitle formatting with OpenAI chat.
 *
 * @deprecated replaced by {@link com.kanjimaster.ai.claude.ClaudeSubtitleFormatter}; kept until Claude is live.
 */
@Deprecated
@Component
@ConditionalOnProperty(name = "kanji.ai.provider", havingValue = "openai")
public class OpenAiSubtitleFormatter implements SubtitleFormatter {
    private final OpenAiHttpClient client;
    private final JsonMapper jsonMapper;
    private final Map<String, Object> schema = AiSchemas.load(AiSchemas.SUBTITLES);

    public OpenAiSubtitleFormatter(OpenAiHttpClient client, JsonMapper jsonMapper) {
        this.client = client;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public SubtitleWrapDto format(String srt) {
        String json = client.chatJson("JapaneseTranscriptBuilder", schema, AiPrompts.SUBTITLE_SYSTEM,
                AiPrompts.subtitleUserPrompt(srt));
        try {
            return jsonMapper.readValue(json, SubtitleWrapDto.class);
        } catch (JacksonException e) {
            throw new AiGenerationException("OpenAI returned malformed subtitle JSON", e);
        }
    }
}
