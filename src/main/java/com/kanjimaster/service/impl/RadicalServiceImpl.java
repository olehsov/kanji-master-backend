package com.kanjimaster.service.impl;

import com.kanjimaster.model.updated.Radical;
import com.kanjimaster.repository.RadicalRepository;
import com.kanjimaster.service.RadicalService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RadicalServiceImpl implements RadicalService {
    private final RadicalRepository repository;

    public RadicalServiceImpl(RadicalRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<Radical> findAllRadicals() {
        return repository.findAll();
    }
}
