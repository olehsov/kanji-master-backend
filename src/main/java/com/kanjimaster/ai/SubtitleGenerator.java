package com.kanjimaster.ai;

import com.kanjimaster.dto.SubtitleWrapDto;

/** Produces translated, annotated subtitles for a YouTube video. */
public interface SubtitleGenerator {
    SubtitleWrapDto generate(String youtubeId);

    /** Whether results are real and may be cached in the database. Stub output must not be cached. */
    default boolean isCacheable() {
        return true;
    }
}
