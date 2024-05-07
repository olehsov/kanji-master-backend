package com.kanjimaster.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanjimaster.initializer.KanjiDataInitializer;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class KanjiConfig {
    private final KanjiDataInitializer kanjiDataInitializer;

    public KanjiConfig(KanjiDataInitializer kanjiDataInitializer) {
        this.kanjiDataInitializer = kanjiDataInitializer;
    }

    @Bean(name = "importProcessor")
    public SmartInitializingSingleton importProcessor() {
        return () -> {
            try {
                kanjiDataInitializer.initialize();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

    }
}
