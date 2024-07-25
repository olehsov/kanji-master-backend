package com.kanjimaster.repository;

import com.kanjimaster.model.updated.KanjiInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

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



}
