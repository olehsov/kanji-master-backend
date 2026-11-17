package com.kanjimaster.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** How many tasks of a given type to generate for a grammar topic. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrammarSettingPayloadDto {
    private TaskType type;
    private int taskAmount;
}
