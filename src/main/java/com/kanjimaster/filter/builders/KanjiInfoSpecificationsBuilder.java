package com.kanjimaster.filter.builders;

import com.kanjimaster.filter.FilterType;
import com.kanjimaster.filter.SearchCriteria;
import com.kanjimaster.filter.abstracts.SpecificationsBuilder;
import com.kanjimaster.filter.specifications.KanjiIfoSpecification;
import com.kanjimaster.model.updated.KanjiInfo;

import java.util.List;

public class KanjiInfoSpecificationsBuilder extends SpecificationsBuilder<KanjiInfo, KanjiIfoSpecification> {

    public KanjiInfoSpecificationsBuilder(
            List<String> grade,
            List<String> jlpt,
            List<String> kanjies
    ) throws Exception {
        super();
        if (!grade.isEmpty())
            with("grade", FilterType.IN, grade);
        if (!jlpt.isEmpty())
            with("jlpt", FilterType.IN, jlpt);
        if (!kanjies.isEmpty())
            with("kanji", FilterType.IN, kanjies);
    }

    @Override
    protected KanjiIfoSpecification specificationFactory(SearchCriteria criteria) {
        return new KanjiIfoSpecification(criteria);
    }


}