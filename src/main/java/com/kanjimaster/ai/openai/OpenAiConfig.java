package com.kanjimaster.ai.openai;

import com.kanjimaster.ai.AiProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** OpenAI is needed for Whisper transcription whenever real (non-stub) subtitles are generated. */
@Configuration(proxyBeanMethods = false)
@ConditionalOnExpression("'${kanji.ai.provider:stub}' != 'stub'")
public class OpenAiConfig {

    @Bean
    public OpenAiHttpClient openAiHttpClient(AiProperties properties) {
        return new OpenAiHttpClient(properties.openai());
    }

    @Bean
    public OpenAiWhisperTranscriber openAiWhisperTranscriber(OpenAiHttpClient client) {
        return new OpenAiWhisperTranscriber(client);
    }
}
