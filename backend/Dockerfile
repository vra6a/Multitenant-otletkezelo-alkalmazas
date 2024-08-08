# Use a multi-stage build to create a leaner final image
FROM gradle:7.4.2-jdk11 AS builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM openjdk:11-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/moa-backend-0.0.1-SNAPSHOT.jar app.jar
COPY wait-for-it.sh /app/
RUN chmod +x /app/wait-for-it.sh
EXPOSE 8080
ENTRYPOINT ["./wait-for-it.sh", "mysqldb:3306", "--timeout=30", "--", "java", "-jar", "app.jar"]
