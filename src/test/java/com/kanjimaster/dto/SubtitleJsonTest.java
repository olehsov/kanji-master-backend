package com.kanjimaster.dto;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** The frontend reads these exact JSON field names from /api/subtitle. */
class SubtitleJsonTest {
    private final JsonMapper mapper = JsonMapper.builder().build();

    @Test
    void subtitleJsonUsesFrontendFieldNames() {
        SubtitleWrapDto dto = new SubtitleWrapDto(List.of(SubtitleDto.builder()
                .index(1).timingStart("00:00:01,000").timingEnd("00:00:02,000").line("猫").translation("кіт")
                .kanjies("猫").grammars("")
                .individualTranslations(List.of(IndividualTranslationDto.builder()
                        .text("猫").textHiragana("ねこ").meaning("кіт").isWord(true).jlpt("N4").build()))
                .build()));

        JsonNode line = mapper.valueToTree(dto).get("subtitles").get(0);

        assertThat(line.get("index").asInt()).isEqualTo(1);
        assertThat(line.get("timingStart").asString()).isEqualTo("00:00:01,000");
        JsonNode translation = line.get("individualTranslations").get(0);
        assertThat(translation.get("isWord").asBoolean()).isTrue();
        assertThat(translation.get("textHiragana").asString()).isEqualTo("ねこ");
    }

    @Test
    void roundTripsThroughEntities() {
        SubtitleDto original = SubtitleDto.builder().index(2).line("犬").translation("пес")
                .individualTranslations(List.of(IndividualTranslationDto.builder().text("犬").isWord(true).build()))
                .build();

        SubtitleDto copy = SubtitleDto.fromVideoSubtitle(original.toVideoSubtitle());

        assertThat(copy).usingRecursiveComparison().isEqualTo(original);
    }
}
