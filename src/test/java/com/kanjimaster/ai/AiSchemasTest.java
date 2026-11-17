package com.kanjimaster.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AiSchemasTest {

    @ParameterizedTest
    @ValueSource(strings = {AiSchemas.GRAMMAR_TASK, AiSchemas.SUBTITLES})
    void schemasAreStrictObjects(String name) {
        Map<String, Object> schema = AiSchemas.load(name);

        // Structured outputs (Claude and OpenAI strict mode) require closed objects with explicit required fields.
        assertThat(schema).containsEntry("type", "object").containsEntry("additionalProperties", false)
                .containsKey("required");
    }

    @Test
    void unknownSchemaFails() {
        assertThatThrownBy(() -> AiSchemas.load("missing")).isInstanceOf(RuntimeException.class);
    }
}
