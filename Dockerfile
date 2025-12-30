FROM gradle:8.7.0-jdk17 AS build
WORKDIR /app

COPY . .

RUN gradle wrapper

RUN ./gradlew --no-daemon clean bootJar

# Stage 2: Run
FROM eclipse-temurin:17-jre
WORKDIR /app

EXPOSE 8000

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
