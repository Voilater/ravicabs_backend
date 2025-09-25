# ---------- Build Stage ----------
FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app

# Copy everything (Gradle wrapper + source)
COPY . .

# Build the project, skip tests for faster image build
RUN gradle clean build -x test

# ---------- Runtime Stage -----------
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy the fat JAR from build stage
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar

# Expose port (only needed if your app runs HTTP, e.g. Spring Boot)
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
