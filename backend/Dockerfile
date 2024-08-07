FROM gradle:7.4.2-jdk11 AS builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM openjdk:11-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/moa-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]