package com.kanjimaster.repository;


import com.kanjimaster.model.updated.Radical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RadicalRepository  extends JpaRepository<Radical, String> {
}
