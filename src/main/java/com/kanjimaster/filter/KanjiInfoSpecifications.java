package com.kanjimaster.filter;

import com.kanjimaster.model.updated.KanjiInfo;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

/**
 * JPA specifications used to filter {@link KanjiInfo}.
 */
public final class KanjiInfoSpecifications {

    private KanjiInfoSpecifications() {
    }

    /**
     * Matches kanji whose {@code attribute} is one of {@code values}. An empty or null collection means
     * "no constraint" and returns a specification that matches everything.
     */
    public static Specification<KanjiInfo> attributeIn(String attribute, Collection<String> values) {
        if (values == null || values.isEmpty()) {
            return Specification.unrestricted();
        }
        return (root, query, builder) -> root.get(attribute).in(values);
    }
}
