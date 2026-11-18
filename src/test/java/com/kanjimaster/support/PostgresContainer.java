package com.kanjimaster.support;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Shared Postgres for integration tests. Flyway applies all migrations (V1..V16) on startup, so tests run
 * against the real dictionary data. Requires Docker or Podman.
 * <p>
 * One container is shared by every test context in the JVM: it is faster, and starting a second container
 * in the same run is flaky on Podman machines (dropped API connections).
 */
@TestConfiguration(proxyBeanMethods = false)
public class PostgresContainer {

    private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17-alpine");

    /** Not stopped when a Spring test context closes; Testcontainers (Ryuk) removes it when the JVM exits. */
    @Bean(destroyMethod = "")
    @ServiceConnection
    PostgreSQLContainer postgres() {
        return POSTGRES;
    }
}
