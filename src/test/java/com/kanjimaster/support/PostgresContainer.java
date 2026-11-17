package com.kanjimaster.support;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Shared Postgres for integration tests. Flyway applies all migrations (V1..V16) on startup, so tests run
 * against the real dictionary data. Requires Docker.
 */
@TestConfiguration(proxyBeanMethods = false)
public class PostgresContainer {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgres() {
        return new PostgreSQLContainer("postgres:17-alpine").withReuse(true);
    }
}
