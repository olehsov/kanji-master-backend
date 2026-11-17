package com.kanjimaster.ai.claude;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.kanjimaster.ai.AiProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "kanji.ai.provider", havingValue = "claude")
public class ClaudeConfig {

    @Bean(destroyMethod = "close")
    public AnthropicClient anthropicClient(AiProperties properties) {
        String apiKey = properties.anthropic().apiKey();
        if (!StringUtils.hasText(apiKey)) {
            throw new IllegalStateException("kanji.ai.provider=claude requires ANTHROPIC_API_KEY (kanji.ai.anthropic.api-key)");
        }
        return AnthropicOkHttpClient.builder().apiKey(apiKey).build();
    }
}
