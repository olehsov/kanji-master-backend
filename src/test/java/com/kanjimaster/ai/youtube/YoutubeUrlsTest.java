package com.kanjimaster.ai.youtube;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class YoutubeUrlsTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=qal34e9v_pk",
            "http://youtube.com/watch?v=qal34e9v_pk",
            "www.youtube.com/watch?v=qal34e9v_pk",
            "https://m.youtube.com/watch?v=qal34e9v_pk",
            "https://www.youtube.com/watch?feature=share&v=qal34e9v_pk&t=10",
            "https://youtu.be/qal34e9v_pk",
            "https://youtu.be/qal34e9v_pk?si=abc",
            "https://www.youtube.com/embed/qal34e9v_pk",
            "https://www.youtube.com/shorts/qal34e9v_pk",
            "  https://www.youtube.com/watch?v=qal34e9v_pk  "
    })
    void extractsIdFromSupportedLinks(String url) {
        assertThat(YoutubeUrls.extractVideoId(url)).contains("qal34e9v_pk");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "--exec rm -rf /",
            "https://evil.com/watch?v=qal34e9v_pk",
            "https://youtube.com.evil.com/watch?v=qal34e9v_pk",
            "https://www.youtube.com/watch?v=short",
            "https://www.youtube.com/watch?v=qal34e9v_pk;ls",
            "https://www.youtube.com/channel/UCabcdefghijk",
            "not a url at all"
    })
    void rejectsAnythingElse(String url) {
        assertThat(YoutubeUrls.extractVideoId(url)).isEmpty();
    }

    @Test
    void canonicalUrlUsesWatchFormat() {
        assertThat(YoutubeUrls.canonicalUrl("qal34e9v_pk")).isEqualTo("https://www.youtube.com/watch?v=qal34e9v_pk");
    }
}
