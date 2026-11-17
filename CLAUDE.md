# CLAUDE.md — kanji-master-backend

Backend for **Kanji Master**, a Japanese-learning app for Ukrainian speakers. It serves a kanji dictionary
(kanji, radicals, words, example sentences), generates printable kanji writing-practice PDFs, AI grammar lessons,
and study subtitles for YouTube videos. The Angular frontend lives in `../kanji-master-frontend`.

## Stack
- Java 25 (Gradle toolchain; foojay downloads the JDK if missing), Gradle 9 wrapper
- Spring Boot 4.1 (webmvc, GraphQL, Data JPA, Validation, Flyway), Hibernate 7, Jackson 3 (`tools.jackson.*`)
- PostgreSQL 17, Flyway migrations, Log4j2 (Logback is excluded)
- iText 9 (+ `font-asian`) for PDFs
- Anthropic Java SDK (Claude) and a small `RestClient` for OpenAI (Whisper)
- springdoc-openapi (Swagger UI) and GraphiQL
- Tests: JUnit 5, Mockito, AssertJ, Spring slice tests, Testcontainers (Postgres), JaCoCo

## Commands
```bash
./gradlew build                 # compile + all tests (integration tests need Docker running)
./gradlew test                  # tests only; *IT classes are skipped automatically without Docker
./gradlew bootRun               # needs Postgres on localhost:5432 (see docker-compose.yml)
./gradlew bootRun --args='--spring.profiles.active=dev'   # SQL + HTTP client debug logging
docker compose up --build       # Postgres + backend on http://localhost:8080
```
Coverage report: `build/reports/jacoco/test/html/index.html`.

## API
GraphQL at `POST /graphql` (schema: `src/main/resources/graphql/schema.graphqls`, every type is documented there).
Try queries in **GraphiQL** at `/graphiql`; printed schema at `/graphql/schema`.

| Query | Purpose |
|---|---|
| `getKanjis(page, size, filter)` | Paged kanji list; filter by `search` (kanji typed directly, e.g. `人`, plus kana/romaji readings via SEARCH table), `grade`, `jlpt` (values exactly as stored, e.g. `N5 (beginner)`). Search with no matches → empty page |
| `getKanji(kanji)` | One kanji with its 15 most frequent words; `NOT_FOUND` if missing |
| `getRadicalSearchOptionDto` | Radicals + variants for the radical picker, by stroke count |
| `getKanjiesByRadical(radicals)` | Kanji containing all given radicals (`elements` LIKE match) |
| `getSentencesByKanji(page, size, kanji)` | Example sentences whose `word` contains the kanji |
| `getAllTopics` | Grammar topics |
| `getTopicTask(topicId, settings)` | AI grammar lesson; each `TaskType` at most once, `taskAmount` 0..20, empty settings allowed |

Paging: `page` ≥ 0 (default 0), `size` 1..100 (default 20). Errors use GraphQL `errorType`
`NOT_FOUND` / `BAD_REQUEST` (see `exception/GraphQlExceptionResolver`).

REST (documented in **Swagger UI** at `/swagger-ui.html`, spec at `/v3/api-docs`); errors are RFC 9457 Problem Details:
- `POST /pdf/kanji-practice` — body `KanjiFilter` → PDF attachment (404 if nothing matches)
- `POST /api/subtitle/extract-youtube-video` — body `{"youtubeUrl": "..."}` → subtitles (400 for non-YouTube URLs)

Docs UIs are on by default; set `KANJI_DOCS_ENABLED=false` to turn off GraphiQL, schema printer and Swagger.

## Code layout (`src/main/java/com/kanjimaster`)
- `controller/` — GraphQL `@QueryMapping` controllers + 2 REST controllers. Thin: parse args, call a service.
- `service/` + `service/impl/` — business logic, `@Transactional(readOnly = true)`; `Paging` validates paging.
- `repository/` — Spring Data JPA; native Postgres queries in `KanjiInfoRepository` (`unnest`, `STRING_AGG`, window functions) and `SearchRepository`.
- `model/updated/` — read-only dictionary entities (KanjiInfo, Radical, Radvar, Search, Sentence, Word). `model/` — Topic and video subtitle entities.
- `dto/` — API shapes; never return subtitle/video entities directly.
- `filter/KanjiInfoSpecifications` — JPA specifications for kanji filters.
- `ai/` — AI providers (below). `exception/` — domain exceptions and GraphQL/REST error mapping. `config/` — CORS, OpenAPI.

## AI providers (`kanji.ai.provider`, env `KANJI_AI_PROVIDER`)
| Value | Grammar lessons | Subtitles |
|---|---|---|
| `stub` (**default**) | Fixed demo lesson from `resources/ai/stub/grammar-task.json` (header = requested topic) | Fixed demo subtitles from `resources/ai/stub/subtitles.json`; never cached in the DB |
| `claude` | Claude (`claude-haiku-5-5`, structured outputs) | yt-dlp audio → OpenAI Whisper SRT → Claude formatting/translation; cached per video |
| `openai` (`@Deprecated`) | OpenAI chat with JSON schema | yt-dlp → Whisper → OpenAI chat |

- The stub is deliberate: AI endpoints are public, so they stay on fixed responses until real AI is switched on.
- Claude has no speech-to-text, so Whisper stays on OpenAI for every non-stub provider.
- Output JSON schemas are shared by all providers: `resources/ai/schema/*.json` (closed objects, all fields required).
- Prompts live in `ai/AiPrompts`: instructions in English, learner-facing content in Ukrainian.
- Beans are selected with `@ConditionalOnProperty` / `@ConditionalOnExpression`; controllers depend only on
  `GrammarTaskGenerator` / `SubtitleGenerator` interfaces.
- `yt-dlp` only ever receives a validated 11-char video id as a canonical URL after `--` (`ai/youtube/YoutubeUrls`).

## Configuration (env vars)
| Var | Default | Notes |
|---|---|---|
| `DB_HOST` / `DB_PORT` / `DB_NAME` / `DB_USER` / `DB_PASSWORD` | `localhost` / `5432` / `kanji-master` ×3 | Postgres |
| `APP_PORT` | `8080` | Cloud Run expects 8080 |
| `KANJI_AI_PROVIDER` | `stub` | `stub` \| `claude` \| `openai` |
| `ANTHROPIC_API_KEY` | – | required when provider is `claude` |
| `KANJI_AI_CLAUDE_MODEL` | `claude-haiku-5-5` | |
| `OPENAI_API_TOKEN` | – | Whisper; required when provider is not `stub` |
| `KANJI_DOCS_ENABLED` | `true` | GraphiQL, schema printer, Swagger |

Local secrets: `.env` (from `.env.template`, used by docker compose) or `application-local.yaml` (from
`src/main/resources/application-local.yaml.template`, profile `local`). Both are git-ignored; never commit keys.

CORS origins: `kanji.cors.allowed-origins` in `application.yaml` (no trailing slash).

## Database & migrations
- Flyway, `src/main/resources/db/migration`. V3–V10 load the dictionary data (large SQL files); V11/V14 words and
  kanji↔word links; V13 grammar topics; V15 video subtitles; V16 drops the unused `users` table and makes
  `video_metadata.youtube_id` unique.
- **Never edit an applied migration** — add a new `V{n}__description.sql`. Hibernate runs with `ddl-auto: validate`,
  so entity changes need a matching migration.
- Production predates Flyway running in the app: `baseline-on-migrate` + `baseline-version: 15` mark an existing
  schema as V15 once, then V16+ run automatically. An empty database runs V1..V16.
- `KanjiInfo.words` is lazy; GraphQL resolves it inside the request (open-in-view is on).

## Testing conventions
- Unit tests (`*Test`) with Mockito for services/AI; `@GraphQlTest` and `@WebMvcTest` slices for controllers.
- Integration tests (`*IT`) use `support/PostgresContainer` (`@ServiceConnection`, all migrations, real data) and
  `@Testcontainers(disabledWithoutDocker = true)` — they **skip silently without Docker**; CI runs them.
- Never call real AI APIs in tests: mock `ClaudeJsonClient` / generators; the app context uses the stub.
- Write positive and negative cases; test data builders in `support/TestData`.

## Conventions
- Constructor injection, no field `@Autowired`. Lombok for DTOs/entities (`@Getter/@Setter` on entities, not `@Data`).
- Code, comments, logs and API error messages in English; learner-facing content (AI explanations, translations,
  stub texts) in Ukrainian.
- Throw `NotFoundException` / `BadRequestException` / `AiGenerationException`; don't wrap in `RuntimeException`.
- Keep the GraphQL schema descriptions up to date when changing queries.

## Deployment
- Cloud Run (europe-west1), built automatically from GitHub with the `Dockerfile` (JDK 25 build, JRE 25 runtime,
  non-root, includes `yt-dlp` + `ffmpeg` for the non-stub subtitle pipeline).
- `.github/workflows/docker-publish.yml` runs `./gradlew build` (tests incl. Testcontainers) and publishes an image
  to ghcr.io on `main` and `v*` tags.

## Known tech debt
- No authentication or rate limiting; anything AI-backed must stay behind the stub or get protection first.
- `getKanjis` returns entities; requesting `words` on a page triggers N+1 queries.
- `PDF` endpoint has no limit on the number of kanji (an empty filter renders the whole dictionary).
- Entity package name `model/updated` and several GraphQL names (`getRadicalSearchOptionDto`, `getKanjies*`) are
  kept for frontend compatibility.
- `resources/static/source/word-dict` (19 MB EDICT dump) is served as a static file and not used by code.
