package com.kanjimaster.filter.abstracts;

import com.kanjimaster.filter.SearchCriteria;
import com.kanjimaster.filter.FilterType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SpecificationsBuilder<T, S extends Specification> {
    private final List<SearchCriteria> params;

    public SpecificationsBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    protected abstract S specificationFactory(SearchCriteria criteria);

    public SpecificationsBuilder with(String key, FilterType operation, Object value) throws Exception {
        params.add(SearchCriteria.searchCriteriaFactory(key, operation, value));
        return this;
    }

    public Specification<T> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(this::specificationFactory)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
