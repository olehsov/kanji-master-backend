package com.kanjimaster.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.dto.TopicTaskDto;
import com.kanjimaster.model.Topic;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAiClientService {
    private final OpenAiChatModel openAiChatModel;
    private final ObjectMapper objectMapper;

    public OpenAiClientService(OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
        this.objectMapper = new ObjectMapper();
    }

    public TopicTaskDto getTopicTasks(Topic topic, List<GrammarSettingPayloadDto> settings) throws JsonProcessingException {
        OpenAiChatOptions options = (OpenAiChatOptions) openAiChatModel.getDefaultOptions();
        options.getResponseFormat().setSchema(null);

        StringBuilder builder = new StringBuilder(topic.getHeader() + ".");

        settings.forEach(setting -> builder.append(String.format(
                " Should be %d items with %s type.",
                setting.getTaskAmount(),
                setting.getType()
        )));

        ChatResponse response = openAiChatModel.call(new Prompt(builder.toString()));

        TopicTaskDto topicTaskDto = objectMapper.readValue(response.getResult().getOutput().getContent(), TopicTaskDto.class);
        topicTaskDto.setHeader(topic.getHeader());
        return topicTaskDto;
    }
}
