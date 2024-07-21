package com.kanjimaster.dto;

import com.kanjimaster.model.updated.Radical;
import com.kanjimaster.model.updated.Radvar;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RadicalSearchOptionDto {
    private String radical;
    private int strokes;
    private boolean isSimplifiedRadical;

    public static RadicalSearchOptionDto fromBaseRadical(Object radical) {
        if (radical instanceof Radvar) {
            return RadicalSearchOptionDto.builder()
                    .radical(((Radvar) radical).getRadvar())
                    .strokes(((Radvar) radical).getStrokes())
                    .isSimplifiedRadical(true)
                    .build();
        } else if (radical instanceof Radical) {
            return RadicalSearchOptionDto.builder()
                    .radical(((Radical) radical).getRadical())
                    .strokes(((Radical) radical).getStrokes())
                    .isSimplifiedRadical(false)
                    .build();
        }
        return null;
    }
}
