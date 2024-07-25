package com.kanjimaster.repository;

import com.kanjimaster.model.updated.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Search, String> {
    @Query(value = "SELECT S.KANJI FROM SEARCH S WHERE S.READING LIKE CONCAT('%', :search, '%')", nativeQuery = true)
    List<String> findAllKanjiBySearch(@Param("search") String search);
}
