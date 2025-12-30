package com.kanjimaster.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubtitleLIneDto {
    private int order;
    private String subtitle;
    private String from;
    private String to;
}
