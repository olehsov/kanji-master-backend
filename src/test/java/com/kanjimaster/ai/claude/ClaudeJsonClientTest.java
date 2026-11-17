package com.kanjimaster.ai.claude;

import com.anthropic.models.messages.StopReason;
import com.kanjimaster.exception.AiGenerationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClaudeJsonClientTest {

    @Test
    void joinsTextBlocksOnNormalStop() {
        assertThat(ClaudeJsonClient.extractJson(StopReason.END_TURN, List.of("{\"a\":", "1}"))).isEqualTo("{\"a\":1}");
    }

    @Test
    void refusalIsReported() {
        assertThatThrownBy(() -> ClaudeJsonClient.extractJson(StopReason.REFUSAL, List.of("")))
                .isInstanceOf(AiGenerationException.class).hasMessageContaining("declined");
    }

    @Test
    void truncatedOutputIsReported() {
        assertThatThrownBy(() -> ClaudeJsonClient.extractJson(StopReason.MAX_TOKENS, List.of("{\"a\":")))
                .isInstanceOf(AiGenerationException.class).hasMessageContaining("token limit");
    }

    @Test
    void emptyOutputIsReported() {
        assertThatThrownBy(() -> ClaudeJsonClient.extractJson(StopReason.END_TURN, List.of()))
                .isInstanceOf(AiGenerationException.class).hasMessageContaining("empty");
    }
}
