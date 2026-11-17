package com.kanjimaster.ai;

import com.kanjimaster.dto.GrammarSettingPayloadDto;
import com.kanjimaster.model.Topic;

import java.util.List;

/** Prompts shared by every text provider. */
public final class AiPrompts {

    public static final String GRAMMAR_SYSTEM = """
            You are a Japanese teacher for Ukrainian-speaking learners.
            For the given grammar topic, explain how it is formed (formation, formatted with simple HTML),
            give example sentences with translations, and create the requested exercises.
            Write every explanation, description and translation in Ukrainian; Japanese text stays in Japanese.
            Return only data that matches the provided JSON schema.""";

    public static final String SUBTITLE_SYSTEM = """
            You turn a Japanese SRT transcript into structured study subtitles for Ukrainian-speaking learners.
            Keep the original timing. Translate every line fully into Ukrainian. Merge or shorten lines that repeat
            for a long time. List the kanji and grammar used in each line, and translate each word or phrase.
            If a value is unknown, use an empty string. Return only data that matches the provided JSON schema.""";

    private AiPrompts() {
    }

    public static String grammarUserPrompt(Topic topic, List<GrammarSettingPayloadDto> settings) {
        StringBuilder builder = new StringBuilder("Grammar topic: ").append(topic.getHeader()).append('\n');
        if (topic.getDescription() != null) {
            builder.append("Topic description: ").append(topic.getDescription()).append('\n');
        }
        builder.append("Exercises to create:\n");
        settings.forEach(setting -> builder.append("- ")
                .append(setting.getTaskAmount())
                .append(" tasks of type ")
                .append(setting.getType().name())
                .append('\n'));
        return builder.toString();
    }

    public static String subtitleUserPrompt(String srt) {
        return "Transcript (SRT):\n" + srt;
    }
}
