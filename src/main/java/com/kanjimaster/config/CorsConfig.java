package com.kanjimaster.config;

import org.springframework.http.HttpMethod;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Configuration
public class CorsConfig {
    private final List<String> ALLOWED_ORIGINS;
    private final List<String> ALLOWED_METHODS;
    private final List<String> ALLOWED_HEADERS;

    public CorsConfig() {
        this.ALLOWED_ORIGINS = List.of("http://localhost:4200");
        this.ALLOWED_METHODS = List.of(HttpMethod.GET.name(), HttpMethod.POST.name());
        this.ALLOWED_HEADERS = List.of("*");
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow requests from these origins
        config.setAllowedOrigins(ALLOWED_ORIGINS);

        // Allow these HTTP methods (e.g., GET, POST)
        config.setAllowedMethods(ALLOWED_METHODS);

        // Allow these headers
        config.setAllowedHeaders(ALLOWED_HEADERS);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
