package com.kanjimaster.ai.claude;

import com.anthropic.client.AnthropicClient;
import com.anthropic.core.JsonValue;
import com.anthropic.core.http.StreamResponse;
import com.anthropic.errors.AnthropicException;
import com.anthropic.helpers.MessageAccumulator;
import com.anthropic.models.messages.JsonOutputFormat;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.OutputConfig;
import com.anthropic.models.messages.RawMessageStreamEvent;
import com.anthropic.models.messages.StopReason;
import com.anthropic.models.messages.TextBlock;
import com.kanjimaster.ai.AiProperties;
import com.kanjimaster.exception.AiGenerationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Sends one prompt to Claude and returns JSON constrained to a schema (structured outputs).
 * Streams the response so long subtitle outputs don't hit HTTP timeouts.
 */
@Component
@ConditionalOnProperty(name = "kanji.ai.provider", havingValue = "claude")
public class ClaudeJsonClient {
    private final AnthropicClient client;
    private final AiProperties.Anthropic properties;

    public ClaudeJsonClient(AnthropicClient client, AiProperties properties) {
        this.client = client;
        this.properties = properties.anthropic();
    }

    public String generateJson(String systemPrompt, String userPrompt, Map<String, Object> schema) {
        JsonOutputFormat.Schema.Builder schemaBuilder = JsonOutputFormat.Schema.builder();
        schema.forEach((key, value) -> schemaBuilder.putAdditionalProperty(key, JsonValue.from(value)));

        MessageCreateParams params = MessageCreateParams.builder()
                .model(properties.model())
                .maxTokens(properties.maxTokens())
                .system(systemPrompt)
                .outputConfig(OutputConfig.builder()
                        .effort(OutputConfig.Effort.LOW)
                        .format(JsonOutputFormat.builder().schema(schemaBuilder.build()).build())
                        .build())
                .addUserMessage(userPrompt)
                .build();

        Message message;
        try (StreamResponse<RawMessageStreamEvent> stream = client.messages().createStreaming(params)) {
            MessageAccumulator accumulator = MessageAccumulator.create();
            stream.stream().forEach(accumulator::accumulate);
            message = accumulator.message();
        } catch (AnthropicException e) {
            throw new AiGenerationException("Claude request failed: " + e.getMessage(), e);
        }

        return extractJson(message.stopReason().orElse(null), message.content().stream()
                .flatMap(block -> block.text().stream())
                .map(TextBlock::text)
                .toList());
    }

    /** Validates the stop reason and joins the text blocks into the JSON payload. */
    static String extractJson(StopReason stopReason, List<String> textBlocks) {
        if (StopReason.REFUSAL.equals(stopReason)) {
            throw new AiGenerationException("Claude declined to generate this content");
        }
        if (StopReason.MAX_TOKENS.equals(stopReason)) {
            throw new AiGenerationException("Claude response was cut off at the token limit");
        }
        String json = String.join("", textBlocks);
        if (json.isBlank()) {
            throw new AiGenerationException("Claude returned an empty response");
        }
        return json;
    }
}
