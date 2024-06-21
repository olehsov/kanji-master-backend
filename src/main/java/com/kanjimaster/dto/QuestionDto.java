package com.kanjimaster.dto;

import com.kanjimaster.model.Kanji;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {
    private String text;
    private Set<String> answers;
    private Kanji kanji;
}
