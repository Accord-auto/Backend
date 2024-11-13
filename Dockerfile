FROM gradle:8.10.2 AS builder

WORKDIR /app

COPY . .

RUN gradle build

FROM openjdk:21-jdk

EXPOSE 8080

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]