package com.kanjimaster.controller;

import com.kanjimaster.service.WordService;
import org.springframework.stereotype.Controller;

@Controller
public class WordController {
    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }
}
