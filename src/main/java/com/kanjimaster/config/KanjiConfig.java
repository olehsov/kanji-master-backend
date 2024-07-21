package com.kanjimaster.config;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KanjiConfig {
    @Bean(name = "importProcessor")
    public SmartInitializingSingleton importProcessor() {
        return () -> {};
    }
}
