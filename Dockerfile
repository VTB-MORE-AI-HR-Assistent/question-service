# Build stage
FROM gradle:8.5-jdk21-alpine AS builder

WORKDIR /app

# Copy gradle configuration
COPY build.gradle.kts settings.gradle.kts ./

# Download dependencies (cached layer)
RUN gradle dependencies --no-daemon || true

# Copy source code
COPY src src

# Build the application
RUN gradle bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache dumb-init

WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the JAR file
COPY --from=builder /app/build/libs/*.jar app.jar
RUN chown spring:spring app.jar

USER spring:spring

EXPOSE 8085

ENTRYPOINT ["dumb-init", "--", "java", "-jar", "app.jar"]