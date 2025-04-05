package com.kanjimaster.initializer;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;

@Component
@Log4j2
public class WordDataInitializer {
    public WordDataInitializer() {}

    @Transactional
    public void initialize() throws IOException {
    }
}
