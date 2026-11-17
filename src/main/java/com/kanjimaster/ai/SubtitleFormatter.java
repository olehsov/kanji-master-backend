package com.kanjimaster.ai;

import com.kanjimaster.dto.SubtitleWrapDto;

/** Turns a raw Japanese SRT transcript into structured, translated subtitles. */
public interface SubtitleFormatter {
    SubtitleWrapDto format(String srt);
}
