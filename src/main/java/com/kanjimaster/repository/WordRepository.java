package com.kanjimaster.repository;

import com.kanjimaster.model.updated.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Page<Word> findAllByTranslation(String translation, Pageable pageable);
    List<Word> findAllByTranslation(String translation);
}
