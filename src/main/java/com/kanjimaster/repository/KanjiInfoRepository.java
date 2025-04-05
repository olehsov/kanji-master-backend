package com.kanjimaster.repository;

import com.kanjimaster.model.updated.KanjiInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface KanjiInfoRepository extends JpaRepository<KanjiInfo, String>, JpaSpecificationExecutor<KanjiInfo> {
    @Query(value = "WITH search_terms AS (" +
            "  SELECT unnest(:radicals) AS term" +
            ")" +
            "SELECT k.* " +
            "FROM kanji_info k " +
            "JOIN search_terms st ON k.elements LIKE '%' || st.term || '%' " +
            "GROUP BY k.kanji " +
            "HAVING COUNT(DISTINCT st.term) = (SELECT COUNT(*) FROM search_terms)",
            nativeQuery = true)
    Collection<KanjiInfo> findKanjiesByRadicals(@Param("radicals") String[] radicals);
    @Query(value = """
    SELECT ranked.kanji, STRING_AGG(ranked.name_aggreegation, ',') as representation
    FROM (
         SELECT
             CONCAT(w.word, '(', w.romaji_display, ')') as name_aggreegation,
             kw.kanji,
             ROW_NUMBER() OVER (PARTITION BY kw.kanji ORDER BY w.freq DESC) AS rn
         FROM word w
         JOIN kanji_word kw ON w.id = kw.word_id
         WHERE kw.kanji IN :kanjis AND kw.kanji != w.word
    ) ranked
    WHERE ranked.rn <= 3
    GROUP BY ranked.kanji
""", nativeQuery = true)
    List<Object[]> findKanjiWordRepresentations(@Param("kanjis") List<String> kanjis);
}
