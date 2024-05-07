package com.kanjimaster.filter.specifications;

import com.kanjimaster.filter.SearchCriteria;
import com.kanjimaster.filter.abstracts.BaseSpecification;
import com.kanjimaster.model.Kanji;

public class KanjiSpecification extends BaseSpecification<Kanji> {
        public KanjiSpecification(SearchCriteria criteria) {
            super(criteria);
        }
}
