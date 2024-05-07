package com.kanjimaster.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kanjimaster.model.Kanji;
import com.kanjimaster.model.Reading;
import com.kanjimaster.model.Word;
import com.kanjimaster.mappers.interfaces.JsonMapper;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KanjiMapper implements JsonMapper<Kanji> {
    private String kanji;
    private int grade;
    @JsonProperty(value = "stroke_count")
    private Integer strokeCount;
    private Set<String> meanings = new HashSet<>();
    @JsonProperty(value = "heisig_en")
    private String heisigEn;
    @JsonProperty(value = "kun_readings")
    private Set<String> kunReadings = new HashSet<>();
    @JsonProperty(value = "on_readings")
    private Set<String> onReadings = new HashSet<>();
    @JsonProperty(value = "name_readings")
    private Set<String> nameReadings = new HashSet<>();
    private int jlpt;
    private String unicode;
    private Set<String> notes = new HashSet<>();

    @Override
    public Kanji convertToEntity() {
        return Kanji.builder()
                .kanji(this.kanji)
                .grade(this.grade)
                .strokeCount(this.strokeCount)
                .meanings(this.meanings)
                .heisigEn(this.heisigEn)
                .jlpt(this.jlpt)
                .unicode(this.unicode)
                .notes(this.notes)
                .kunReadings(new HashSet<>())
                .onReadings(new HashSet<>())
                .nameReadings(new HashSet<>())
                .words(new HashSet<>())
                .build();
    }

    public Kanji convertToEntity(List<WordMapper> wordMappers) {
        Kanji kanji = this.convertToEntity();

        Set<Word> words = wordMappers.parallelStream()
                .filter(wordMapper -> wordMapper.getKanji().equals(this.kanji))
                .map(WordMapper::convertToEntity)
                .peek(word -> word.setKanji(kanji))
                .collect(Collectors.toSet());

        kanji.getWords().addAll(words);
        return kanji;
    }

    public void updateReadings(Collection<Reading> readings, Kanji kanji) {
        Set<Reading> kunReadings = readings.parallelStream()
                .filter(readingMapper -> this.kunReadings.contains(readingMapper.getReading()))
                .collect(Collectors.toSet());
        Set<Reading> onReadings = readings.parallelStream()
                .filter(readingMapper -> this.onReadings.contains(readingMapper.getReading()))
                .collect(Collectors.toSet());
        Set<Reading> nameReadings = readings.parallelStream()
                .filter(readingMapper -> this.nameReadings.contains(readingMapper.getReading()))
                .collect(Collectors.toSet());

        kanji.getKunReadings().addAll(kunReadings);
        kanji.getOnReadings().addAll(onReadings);
        kanji.getNameReadings().addAll(nameReadings);
    }
}
