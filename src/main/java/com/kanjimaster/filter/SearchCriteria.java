package com.kanjimaster.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private FilterType type;
    private Object value;

    public static SearchCriteria searchCriteriaFactory(String key, FilterType type, Object value) throws Exception {
        if (type.equals(FilterType.BETWEEN) && value != null && value.getClass().getComponentType() == List.class) {
            String typeName = value.getClass().getTypeName();
            throw new Exception("Only array is allowed with `BETWEEN` operator, а у вас " + typeName);
        }
        return new SearchCriteria(key, type, value);
    }
}
