FROM openjdk:23-jdk

EXPOSE 8080

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
