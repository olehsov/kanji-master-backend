package com.kanjimaster.repository;

import com.kanjimaster.model.updated.Sentence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    Page<Sentence> findAllByWordContains(Pageable pageable, String kanji);
}
