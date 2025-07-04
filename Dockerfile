# Use lightweight JVM base image
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY build/libs/harness-datetime-service-*-SNAPSHOT.jar /app/app.jar

ENV HARNESS_FME_KEY="SET_AT_RUNTIME"

# Run the JAR
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
