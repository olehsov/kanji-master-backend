package com.kanjimaster.mappers;

import com.kanjimaster.model.Variant;
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
public class VariantMapper implements JsonMapper<Variant> {
    private List<String> priorities = new ArrayList<>();
    private String pronounced;
    private String written;

    @Override
    public Variant convertToEntity() {
        Set<String> priorities = new HashSet<>(this.priorities);
        return Variant.builder().written(this.written).pronounced(this.pronounced).priorities(priorities).build();
    }
}
