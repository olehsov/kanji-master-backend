package com.kanjimaster.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Swagger UI (/swagger-ui.html) documents the REST endpoints; GraphQL is documented in GraphiQL (/graphiql). */
@Configuration(proxyBeanMethods = false)
public class OpenApiConfig {

    @Bean
    public OpenAPI kanjiMasterOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Kanji Master REST API")
                .version("v1")
                .description("""
                        REST endpoints of the Kanji Master backend (PDF practice sheets, YouTube subtitles).
                        Most data is served over GraphQL at `POST /graphql`: explore and run queries in
                        GraphiQL at [/graphiql](/graphiql), schema at [/graphql/schema](/graphql/schema)."""));
    }
}
