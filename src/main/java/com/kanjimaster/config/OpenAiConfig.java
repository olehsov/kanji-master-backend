package com.kanjimaster.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "openai")
@Getter
@Setter
public class OpenAiConfig {
    private String model;
    private Api api;

    @Getter
    @Setter
    public static class Api {
        private String url;
        private String key;
    }
}
