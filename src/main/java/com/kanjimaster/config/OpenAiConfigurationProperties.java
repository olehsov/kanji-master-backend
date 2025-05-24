package com.kanjimaster.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.ai.openai")
@Getter
public class OpenAiConfigurationProperties {
    @Setter
    private String apiKey;
    @Setter
    private String baseUrl;
    private ChatOptions chat;
    private ChatOptions chatTranscriptAudio;

    @Data
    public static class ChatOptions {
        private String model;
        private ResponseFormat responseFormat;

        @Data
        public static class ResponseFormat {
            private String type;
            private String name;
            private String schema;
        }
    }

    @Data
    public static class Chat {
        private ChatOptions options;
    }

    public void setChat(Chat chat) {
        this.chat = chat.options;
    }

    public void setChatTranscriptAudio(Chat chat) {
        this.chatTranscriptAudio = chat.options;
    }
}
