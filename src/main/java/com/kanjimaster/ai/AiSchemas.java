package com.kanjimaster.ai;

import org.springframework.core.io.ClassPathResource;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Map;

/** JSON schemas for structured AI output, loaded from {@code classpath:ai/schema}. */
public final class AiSchemas {
    public static final String GRAMMAR_TASK = "grammar-task";
    public static final String SUBTITLES = "subtitles";

    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    private AiSchemas() {
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> load(String name) {
        try (InputStream in = new ClassPathResource("ai/schema/" + name + ".json").getInputStream()) {
            return MAPPER.readValue(in, Map.class);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot load AI schema " + name, e);
        }
    }
}
