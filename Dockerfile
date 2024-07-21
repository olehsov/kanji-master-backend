# Stage 1: Build
FROM gradle:8.7.0-jdk17 as build

WORKDIR /app

# Copy the source code to the container
COPY . .
RUN gradle wrapper
# Build the application
RUN ./gradlew bootJar

# Stage 2: Run
FROM openjdk:17

EXPOSE 8000

# Copy the jar file from the build stage
COPY --from=build /app/build/libs/kanji-master-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]