package com.kanjimaster.ai.youtube;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/** Parses YouTube links into an 11-character video id. Anything else is rejected. */
public final class YoutubeUrls {
    private static final Pattern VIDEO_ID = Pattern.compile("[A-Za-z0-9_-]{11}");
    private static final Set<String> YOUTUBE_HOSTS = Set.of("youtube.com", "www.youtube.com", "m.youtube.com",
            "music.youtube.com");
    private static final Set<String> ID_PATH_PREFIXES = Set.of("embed", "v", "shorts", "live");

    private YoutubeUrls() {
    }

    public static Optional<String> extractVideoId(String url) {
        if (url == null || url.isBlank()) {
            return Optional.empty();
        }
        String candidate = url.trim();
        if (!candidate.matches("(?i)^https?://.*")) {
            candidate = "https://" + candidate;
        }
        URI uri;
        try {
            uri = new URI(candidate);
        } catch (URISyntaxException e) {
            return Optional.empty();
        }
        String host = uri.getHost() == null ? "" : uri.getHost().toLowerCase();
        String[] segments = uri.getPath() == null ? new String[0]
                : Arrays.stream(uri.getPath().split("/")).filter(s -> !s.isEmpty()).toArray(String[]::new);

        String id = null;
        if (host.equals("youtu.be") && segments.length >= 1) {
            id = segments[0];
        } else if (YOUTUBE_HOSTS.contains(host)) {
            if (segments.length == 1 && segments[0].equals("watch")) {
                id = queryParam(uri.getRawQuery(), "v");
            } else if (segments.length >= 2 && ID_PATH_PREFIXES.contains(segments[0])) {
                id = segments[1];
            }
        }
        return Optional.ofNullable(id).filter(value -> VIDEO_ID.matcher(value).matches());
    }

    public static String canonicalUrl(String videoId) {
        return "https://www.youtube.com/watch?v=" + videoId;
    }

    private static String queryParam(String rawQuery, String name) {
        if (rawQuery == null) {
            return null;
        }
        return Arrays.stream(rawQuery.split("&"))
                .map(pair -> pair.split("=", 2))
                .filter(pair -> pair.length == 2 && pair[0].equals(name))
                .map(pair -> pair[1])
                .findFirst()
                .orElse(null);
    }
}
