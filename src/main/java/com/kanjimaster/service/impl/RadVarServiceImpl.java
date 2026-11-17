package com.kanjimaster.service.impl;

import com.kanjimaster.model.updated.Radvar;
import com.kanjimaster.repository.RadVarRepository;
import com.kanjimaster.service.RadVarService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
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
