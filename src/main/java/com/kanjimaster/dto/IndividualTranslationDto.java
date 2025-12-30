package com.kanjimaster.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kanjimaster.model.IndividualSubtitleTranslation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonDeserialize
@JsonSerialize
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndividualTranslationDto {
    @JsonProperty("text")
    private String text;

    @JsonProperty("textHiragana")
    private String textHiragana;

    @JsonProperty("meaning")
    private String meaning;

    @JsonProperty("isWord")
    private boolean isWord;

    @JsonProperty("jlpt")
    private String jlpt;

    @JsonIgnore
    public IndividualSubtitleTranslation toIndividualSubtitleTranslation() {
        IndividualSubtitleTranslation individualSubtitleTranslation = new IndividualSubtitleTranslation();
        individualSubtitleTranslation.setText(text);
        individualSubtitleTranslation.setTextHiragana(textHiragana);
        individualSubtitleTranslation.setMeaning(meaning);
        individualSubtitleTranslation.setWord(isWord);
        individualSubtitleTranslation.setJlpt(jlpt);
        return individualSubtitleTranslation;
    }

    @JsonIgnore
    public static IndividualTranslationDto fromIndividualSubtitleTranslation(IndividualSubtitleTranslation individualTranslation) {
        return IndividualTranslationDto.builder()
                .text(individualTranslation.getText())
                .textHiragana(individualTranslation.getTextHiragana())
                .meaning(individualTranslation.getMeaning())
                .isWord(individualTranslation.isWord())
                .jlpt(individualTranslation.getJlpt())
                .build();
    }
}
