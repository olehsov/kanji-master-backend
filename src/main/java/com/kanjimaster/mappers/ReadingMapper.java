package com.kanjimaster.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kanjimaster.model.Reading;
import com.kanjimaster.mappers.interfaces.JsonMapper;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReadingMapper implements JsonMapper<Reading> {
    @JsonProperty(value = "main_kanji")
    private Set<String> mainKanji = new HashSet<>();
    @JsonProperty(value = "name_kanji")
    private Set<String> nameKanji = new HashSet<>();
    private String reading;

    @Override
    public Reading convertToEntity() {
        return Reading.builder()
                .mainKanji(this.mainKanji)
                .nameKanji(this.nameKanji)
                .reading(this.reading)
                .kanjisKun(new HashSet<>())
                .kanjisOn(new HashSet<>())
                .kanjisName(new HashSet<>())
                .build();
    }
}
