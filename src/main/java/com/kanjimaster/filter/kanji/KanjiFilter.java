package com.kanjimaster.filter.kanji;

import lombok.*;

import java.lang.reflect.Array;
import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KanjiFilter {
    private Set<Integer> jlpt = new HashSet<>();
    private Set<Integer> grade = new HashSet<>();
    private Set<Integer> strokeCount = new HashSet<>();

    public List<Integer> getJlptSafe() {
        return KanjiFilter.getSafe(this.jlpt);
    }
    public List<Integer> getGradeSafe() {
        return KanjiFilter.getSafe(this.grade);
    }
    public List<Integer> getStrokeCountSafe() {
        return KanjiFilter.getSafe(this.strokeCount);
    }

    private static List<Integer> getSafe(Set<Integer> value) {
        if (Objects.isNull(value) || value.isEmpty()) {
            return null;
        }
        return new ArrayList<>(value);
    }
}
