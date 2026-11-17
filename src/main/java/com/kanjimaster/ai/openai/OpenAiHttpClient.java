package com.kanjimaster.ai.openai;

import com.kanjimaster.ai.AiProperties;
import com.kanjimaster.exception.AiGenerationException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Minimal OpenAI REST client (Whisper transcription and, for the deprecated provider, JSON-schema chat).
 * Created by {@link OpenAiConfig} only when a provider other than {@code stub} is active.
 */
public class OpenAiHttpClient {
    private final RestClient restClient;
    private final AiProperties.OpenAi properties;

    public OpenAiHttpClient(AiProperties.OpenAi properties) {
        if (!StringUtils.hasText(properties.apiKey())) {
            throw new IllegalStateException("Audio transcription requires OPENAI_API_TOKEN (kanji.ai.openai.api-key)");
        }
        this.properties = properties;
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(
                HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build());
        requestFactory.setReadTimeout(Duration.ofMinutes(5));
        this.restClient = RestClient.builder()
                .baseUrl(properties.baseUrl())
                .requestFactory(requestFactory)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.apiKey())
                .build();
    }

    /** Whisper speech-to-text; returns Japanese subtitles in SRT format. */
    public String transcribeToSrt(Path audioFile) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(audioFile));
        body.add("model", properties.transcriptionModel());
        body.add("response_format", "srt");
        body.add("language", "ja");
        body.add("temperature", "0.3");
        try {
            return restClient.post()
                    .uri("/v1/audio/transcriptions")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException e) {
            throw new AiGenerationException("OpenAI transcription failed: " + e.getMessage(), e);
        }
    }

    /**
     * Chat completion constrained to a JSON schema.
     *
     * @deprecated only used by the legacy {@code openai} provider; use Claude instead.
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public String chatJson(String schemaName, Map<String, Object> schema, String systemPrompt, String userPrompt) {
        Map<String, Object> request = Map.of(
                "model", properties.chatModel(),
                "temperature", 0.3,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)),
                "response_format", Map.of(
                        "type", "json_schema",
                        "json_schema", Map.of("name", schemaName, "strict", true, "schema", schema)));
        try {
            Map<String, Object> response = restClient.post()
                    .uri("/v1/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.getFirst().get("message");
            return (String) message.get("content");
        } catch (RuntimeException e) {
            throw new AiGenerationException("OpenAI chat request failed: " + e.getMessage(), e);
        }
    }
}
