package com.kanjimaster.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

/**
 * AI configuration ({@code kanji.ai.*}).
 */
@ConfigurationProperties(prefix = "kanji.ai")
public record AiProperties(
        @DefaultValue("stub") AiProvider provider,
        @DefaultValue Anthropic anthropic,
        @DefaultValue OpenAi openai,
        @DefaultValue Youtube youtube
) {
    public record Anthropic(
            String apiKey,
            @DefaultValue("claude-haiku-5-5") String model,
            @DefaultValue("32000") long maxTokens
    ) {
    }

    public record OpenAi(
            String apiKey,
            @DefaultValue("https://api.openai.com") String baseUrl,
            @DefaultValue("gpt-4o") String chatModel,
            @DefaultValue("whisper-1") String transcriptionModel
    ) {
    }

    public record Youtube(
            @DefaultValue("yt-dlp") String ytDlpCommand,
            @DefaultValue("10m") Duration downloadTimeout
    ) {
    }
}
