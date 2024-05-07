package com.kanjimaster.filter.builders;

import com.kanjimaster.filter.FilterType;
import com.kanjimaster.filter.SearchCriteria;
import com.kanjimaster.filter.abstracts.SpecificationsBuilder;
import com.kanjimaster.filter.specifications.KanjiSpecification;
import com.kanjimaster.model.Kanji;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KanjiSpecificationsBuilder extends SpecificationsBuilder<Kanji, KanjiSpecification> {

    public KanjiSpecificationsBuilder(
            List<Integer> jlpt,
            List<Integer> grade,
            List<Integer> strokeCount
    ) throws Exception {
        super();
        this.with("jlpt", FilterType.IN, jlpt)
                .with("grade", FilterType.IN, grade)
                .with("strokeCount", FilterType.IN, strokeCount);
    }

    @Override
    protected KanjiSpecification specificationFactory(SearchCriteria criteria) {
        return new KanjiSpecification(criteria);
    }
}
