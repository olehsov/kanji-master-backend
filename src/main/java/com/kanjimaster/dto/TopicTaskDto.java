package com.kanjimaster.dto;

import lombok.Data;

import java.util.List;

@Data
public class TopicTaskDto {
    private String header;
    private String formation;
    private List<ExampleSentence> exampleSentences;
    private List<Task> tasks;

    @Data
    public static class ExampleSentence {
        private String sentence;
        private String translation;
    }

    @Data
    public static class Task {
        private String taskType;
        private String description;
        private String question;
        private String correctAnswer;
        private List<String> resources;
    }
}