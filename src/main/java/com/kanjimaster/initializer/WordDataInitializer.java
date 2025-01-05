package com.kanjimaster.initializer;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;

@Component
public class WordDataInitializer {
    public WordDataInitializer() {}

    @Transactional
    public void initialize() throws IOException {
    }
}
