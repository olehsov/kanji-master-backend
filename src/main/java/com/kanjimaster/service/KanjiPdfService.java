package com.kanjimaster.service;

import com.kanjimaster.model.updated.KanjiInfo;

import java.io.IOException;
import java.util.Map;

public interface KanjiPdfService {
    byte[] buildKanjiLearnFile(Map<KanjiInfo, String> kanjiesMap) throws IOException;
}
