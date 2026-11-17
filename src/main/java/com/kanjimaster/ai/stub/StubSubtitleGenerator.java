package com.kanjimaster.ai.stub;

import com.kanjimaster.ai.SubtitleGenerator;
import com.kanjimaster.dto.SubtitleWrapDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Default provider: returns the same pre-generated subtitles for any video. Never downloads audio and is never
 * cached, so real subtitles can be generated later for the same video.
 */
@Component
@ConditionalOnProperty(name = "kanji.ai.provider", havingValue = "stub", matchIfMissing = true)
public class StubSubtitleGenerator implements SubtitleGenerator {

    @Override
    public SubtitleWrapDto generate(String youtubeId) {
        return StubResources.read("subtitles", SubtitleWrapDto.class);
    }

    @Override
    public boolean isCacheable() {
        return false;
    }
}
