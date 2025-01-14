package com.kanjimaster.dto;

import com.kanjimaster.model.Topic;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicDto {
    private Long id;
    private String header;
    private String description;

    public static TopicDto fromTopic(Topic topic) {
        return TopicDto.builder()
                .id(topic.getId())
                .header(topic.getHeader())
                .description(topic.getDescription())
                .build();
    }
}
