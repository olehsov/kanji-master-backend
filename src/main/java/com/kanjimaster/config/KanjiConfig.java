package com.kanjimaster.config;

import com.kanjimaster.initializer.WordDataInitializer;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class KanjiConfig {
    private final WordDataInitializer wordDataInitializer;

    public KanjiConfig(WordDataInitializer wordDataInitializer) {
        this.wordDataInitializer = wordDataInitializer;
    }

    @Bean(name = "importProcessor")
    public SmartInitializingSingleton importProcessor() {
        return () -> {
            try {
                wordDataInitializer.initialize();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
