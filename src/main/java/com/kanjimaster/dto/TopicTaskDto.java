package com.kanjimaster.dto;

import lombok.Data;

import java.util.List;

/** AI-generated lesson for a grammar topic. Field names match the JSON schema in ai/schema/grammar-task.json. */
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
        private TaskType taskType;
        private String description;
        private String question;
        private String correctAnswer;
        private List<String> resources;
    }
}
