package com.kanjimaster.repository;

import com.kanjimaster.model.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
    Page<Word> findAllByKanji_Id(Long kanjiId, Pageable pageable);
}
