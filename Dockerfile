# Multi-stage build for PostAndComments Spring Boot Application

# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies (this layer will be cached if pom.xml doesn't change)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Create a non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring
USER spring

# Copy the JAR file from build stage
COPY --from=build /app/target/*.jar app.jar

# 👉 Activate docker profile INSIDE the running container
ENV SPRING_PROFILES_ACTIVE=docker

# Expose the application port
EXPOSE 8081

# Set JVM options for better container performance
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check (optional - can be removed if actuator is not configured)
# HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
#   CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/posts || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

