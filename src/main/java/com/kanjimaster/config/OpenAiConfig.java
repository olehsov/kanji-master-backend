package com.kanjimaster.config;

import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;

import static com.kanjimaster.constant.OpenAiConstants.JAPANESE_TASK_BUILDER;
import static com.kanjimaster.constant.OpenAiConstants.JAPANESE_TRANSCRIPT_BUILDER;

@Configuration
public class OpenAiConfig {
    private final OpenAiConfigurationProperties openAiConfigurationProperties;

    public OpenAiConfig(OpenAiConfigurationProperties openAiConfigurationProperties) {
        this.openAiConfigurationProperties = openAiConfigurationProperties;
    }

    @Bean
    public OpenAiApi openAiApi() {
        return new OpenAiApi(openAiConfigurationProperties.getBaseUrl(), openAiConfigurationProperties.getApiKey());
    }

    @Bean
    public OpenAiAudioApi openAiAudioApi(RestClient.Builder restBuilder, ResponseErrorHandler responseErrorHandler) {
        return new OpenAiAudioApi(openAiConfigurationProperties.getBaseUrl(), openAiConfigurationProperties.getApiKey(),
                restBuilder, responseErrorHandler);
    }

    @Bean(JAPANESE_TASK_BUILDER)
    public OpenAiChatModel openAiChatModel(OpenAiApi openAiApi) {
        OpenAiConfigurationProperties.ChatOptions chatOptions = openAiConfigurationProperties.getChat();
        return buildChartModelWithJsonSchema(openAiApi, chatOptions);
    }

    @Bean(JAPANESE_TRANSCRIPT_BUILDER)
    public OpenAiChatModel openAiChatModelSubtitleSchema(OpenAiApi openAiApi) {
        OpenAiConfigurationProperties.ChatOptions chatOptions = openAiConfigurationProperties.getChatSubtitle();
        return buildChartModelWithJsonSchema(openAiApi, chatOptions);
    }

    @Bean()
    public OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel(OpenAiAudioApi openAiAudioApi) {
        OpenAiConfigurationProperties.ChatOptions chatOptions = openAiConfigurationProperties.getChatTranscriptAudio();

        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                .model(chatOptions.getModel())
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.SRT)
                .language("ja")
                .temperature(0.3f)
                .build();

        return new OpenAiAudioTranscriptionModel(openAiAudioApi, options);
    }

    private OpenAiChatModel buildChartModelWithJsonSchema(OpenAiApi openAiApi, OpenAiConfigurationProperties.ChatOptions chatOptions) {
        ResponseFormat.JsonSchema schema = ResponseFormat.JsonSchema.builder()
                .name(chatOptions.getResponseFormat().getName())
                .schema(chatOptions.getResponseFormat().getSchema())
                .strict(true)
                .build();

        ResponseFormat responseFormat = ResponseFormat.builder()
                .jsonSchema(schema)
                .type(ResponseFormat.Type.JSON_SCHEMA)
                .build();

        responseFormat.setSchema(null);

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(chatOptions.getModel())
                .responseFormat(responseFormat)
                .temperature(0.3d)
                .build();
        return new OpenAiChatModel(openAiApi, options);
    }
}
