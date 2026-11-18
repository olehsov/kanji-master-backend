# kanji-master-backend

Backend of **Kanji Master**, an app for learning Japanese in Ukrainian: kanji dictionary with radicals, words and
example sentences, printable kanji practice sheets, grammar lessons and YouTube study subtitles.

Spring Boot 4 · Java 25 · GraphQL · PostgreSQL · Flyway

## Quick start
```bash
docker compose up --build
```
- GraphQL: `POST http://localhost:8080/graphql` — explore in GraphiQL at http://localhost:8080/graphiql
- REST docs (Swagger UI): http://localhost:8080/swagger-ui.html

The first start applies all Flyway migrations, including the dictionary data.

To run from the IDE or Gradle, start only the database (`docker compose up db`) and run:
```bash
./gradlew bootRun
```

## Example query
```graphql
{
  getKanji(kanji: "日") {
    kanji
    meaning
    onyomi
    kunyomi
    words { word yomiDisplay translation }
  }
}
```

## Configuration
Local secrets never go into git: copy `.env.template` to `.env` (read by docker compose) or
`src/main/resources/application-local.yaml.template` to `application-local.yaml` and run with
`./gradlew bootRun --args='--spring.profiles.active=local'`. Both copies are git-ignored.

| Env var | Default | Description |
|---|---|---|
| `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` | `localhost`, `5432`, `kanji-master` | PostgreSQL |
| `APP_PORT` | `8080` | HTTP port |
| `KANJI_AI_PROVIDER` | `stub` | `stub` (fixed demo responses), `claude`, or deprecated `openai` |
| `ANTHROPIC_API_KEY` | – | needed for `claude` |
| `OPENAI_API_TOKEN` | – | Whisper audio transcription, needed for any provider except `stub` |
| `KANJI_DOCS_ENABLED` | `true` | turn GraphiQL / Swagger off |

AI features (grammar lessons, YouTube subtitles) return fixed demo responses by default. See [CLAUDE.md](CLAUDE.md)
for how the providers work.

## Tests
```bash
./gradlew build
```
Integration tests (`*IT`) start PostgreSQL with Testcontainers and need Docker or Podman. Without either they are
skipped.

**Podman (macOS):** install Podman Desktop, create and start a Podman machine (rootful), then point Testcontainers
at it. If Docker Desktop is also installed, `/var/run/docker.sock` belongs to Docker Desktop, so set:
```bash
export DOCKER_HOST="unix://$HOME/.local/share/containers/podman/machine/podman.sock"
export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock
./gradlew test
```
`podman build` and `podman compose up --build` work with the same `Dockerfile` and `docker-compose.yml`. Coverage report: `build/reports/jacoco/test/html/index.html`.

## Deployment
Google Cloud Run, built from this repository's `Dockerfile`. CI (`.github/workflows/docker-publish.yml`) builds,
tests and publishes the image to GitHub Container Registry.
