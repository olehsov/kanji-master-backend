package com.kanjimaster.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.dto.SubtitleWrapDto;
import com.kanjimaster.dto.TopicTaskDto;
import com.kanjimaster.model.Topic;
import com.kanjimaster.model.VideoMetadata;
import com.kanjimaster.model.VideoSubtitle;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.kanjimaster.constant.OpenAiConstants.JAPANESE_TASK_BUILDER;
import static com.kanjimaster.constant.OpenAiConstants.JAPANESE_TRANSCRIPT_BUILDER;

@Service
public class OpenAiClientService {
    private final OpenAiChatModel openAiChatModel;
    private final OpenAiChatModel openAiChatTranscriptModel;
    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
    private final ObjectMapper objectMapper;

    public OpenAiClientService(@Qualifier(JAPANESE_TASK_BUILDER) OpenAiChatModel openAiChatModel,
                               @Qualifier(JAPANESE_TRANSCRIPT_BUILDER) OpenAiChatModel openAiChatTranscriptModel,
                               OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel
    ) {
        this.openAiChatModel = openAiChatModel;
        this.openAiChatTranscriptModel = openAiChatTranscriptModel;
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
        this.objectMapper = new ObjectMapper();
    }

    public TopicTaskDto getTopicTasks(Topic topic, List<GrammarSettingPayloadDto> settings) throws JsonProcessingException {
        StringBuilder builder = new StringBuilder(topic.getHeader() + ".");

        settings.forEach(setting -> builder.append(String.format(
                " Повинно бути %d елементів з %s типом.",
                setting.getTaskAmount(),
                setting.getType()
        )));

        builder.append("Всі пояснення мають бути на українській");
        ChatResponse response = openAiChatModel.call(new Prompt(builder.toString()));

        TopicTaskDto topicTaskDto = objectMapper.readValue(response.getResult().getOutput().getContent(), TopicTaskDto.class);
        topicTaskDto.setHeader(topic.getHeader());
        return topicTaskDto;
    }

    public String processVideoToVideoTranscription(Resource resource) {
        return openAiAudioTranscriptionModel.call(resource);
    }

    public SubtitleWrapDto getSubtitleWrapDto(String subtitles) throws JsonProcessingException {
        Prompt prompt = new Prompt(
                "Переформатуй ці субтитри в заданій схемі, переклад повинен бути на українську. Скороти субтитри які повторюються дуже довго. Якщо якогось значення немає залиши \"\" \n" + subtitles
        );

        ChatResponse response = openAiChatTranscriptModel.call(new Prompt(prompt.toString()));

        return objectMapper.readValue(response.getResult().getOutput().getContent(), SubtitleWrapDto.class);
    }
}
