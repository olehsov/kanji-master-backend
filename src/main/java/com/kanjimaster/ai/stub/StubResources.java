package com.kanjimaster.ai.stub;

import org.springframework.core.io.ClassPathResource;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/** Reads pre-generated responses from {@code classpath:ai/stub}. */
final class StubResources {
    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    private StubResources() {
    }

    static <T> T read(String name, Class<T> type) {
        try (InputStream in = new ClassPathResource("ai/stub/" + name + ".json").getInputStream()) {
            return MAPPER.readValue(in, type);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot load AI stub " + name, e);
        }
    }
}
