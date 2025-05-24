package com.kanjimaster.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.dto.TopicTaskDto;
import com.kanjimaster.model.Topic;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAiClientService {
    private final OpenAiChatModel openAiChatModel;
    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
    private final ObjectMapper objectMapper;

    public OpenAiClientService(OpenAiChatModel openAiChatModel, OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel) {
        this.openAiChatModel = openAiChatModel;
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
}
