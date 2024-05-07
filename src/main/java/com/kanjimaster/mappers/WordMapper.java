package com.kanjimaster.mappers;

import com.kanjimaster.model.Meaning;
import com.kanjimaster.model.Variant;
import com.kanjimaster.model.Word;
import com.kanjimaster.mappers.interfaces.JsonMapper;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WordMapper implements JsonMapper<Word> {
    private String kanji;
    private List<MeaningMapper> meanings = new ArrayList<>();
    private List<VariantMapper> variants = new ArrayList<>();

    @Override
    public Word convertToEntity() {
        Set<Meaning> meanings = this.getMeanings().stream()
                .map(MeaningMapper::convertToEntity)
                .collect(Collectors.toSet());
        Set<Variant> variants = this.getVariants().stream()
                .map(VariantMapper::convertToEntity)
                .collect(Collectors.toSet());
        return Word.builder().meanings(meanings).variants(variants).build();
    }
}
