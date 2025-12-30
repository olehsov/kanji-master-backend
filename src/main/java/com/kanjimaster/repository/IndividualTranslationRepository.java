package com.kanjimaster.repository;

import com.kanjimaster.model.IndividualSubtitleTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualTranslationRepository extends JpaRepository<IndividualSubtitleTranslation, Long> {
}
