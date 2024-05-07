package com.kanjimaster.mappers;

import com.kanjimaster.model.Meaning;
import com.kanjimaster.mappers.interfaces.JsonMapper;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MeaningMapper implements JsonMapper<Meaning> {
    private List<String> glosses = new ArrayList<>();;

    @Override
    public Meaning convertToEntity() {
        Set<String> glosses = new HashSet<>(this.glosses);
        return Meaning.builder().glosses(glosses).build();
    }
}
