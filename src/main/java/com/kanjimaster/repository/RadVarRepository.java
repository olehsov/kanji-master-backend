package com.kanjimaster.repository;

import com.kanjimaster.model.updated.Radvar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RadVarRepository extends JpaRepository<Radvar, String> {
}
