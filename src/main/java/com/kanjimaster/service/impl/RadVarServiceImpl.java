package com.kanjimaster.service.impl;

import com.kanjimaster.model.updated.Radvar;
import com.kanjimaster.repository.RadVarRepository;
import com.kanjimaster.service.RadVarService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RadVarServiceImpl implements RadVarService {
    private final RadVarRepository repository;

    public RadVarServiceImpl(RadVarRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<Radvar> findAllRadVars() {
        return repository.findAll();
    }
}
