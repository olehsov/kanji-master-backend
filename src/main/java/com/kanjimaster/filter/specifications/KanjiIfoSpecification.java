package com.kanjimaster.filter.specifications;

import com.kanjimaster.filter.SearchCriteria;
import com.kanjimaster.filter.abstracts.BaseSpecification;
import com.kanjimaster.model.updated.KanjiInfo;

public class KanjiIfoSpecification extends BaseSpecification<KanjiInfo> {
    public KanjiIfoSpecification(SearchCriteria criteria) {
        super(criteria);
    }
}