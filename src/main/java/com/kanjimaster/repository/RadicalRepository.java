package com.kanjimaster.repository;


import com.kanjimaster.model.updated.Radical;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RadicalRepository  extends JpaRepository<Radical, String> {
}
