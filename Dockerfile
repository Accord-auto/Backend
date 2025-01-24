FROM bellsoft/liberica-openjdk-debian:latest AS builder
WORKDIR /application
COPY . .
RUN --mount=type=cache,target=/root/.gradle  chmod +x gradlew && ./gradlew clean build -x test

FROM bellsoft/liberica-openjre-debian:23.0.1 AS layers
WORKDIR /application
COPY --from=builder /application/build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM bellsoft/liberica-openjre-debian:23.0.1
VOLUME /tmp
RUN useradd -ms /bin/bash spring-user
RUN mkdir -p /uploads && chown spring-user:spring-user /uploads
USER spring-user
COPY --from=layers /application/dependencies/ ./
COPY --from=layers /application/spring-boot-loader/ ./
COPY --from=layers /application/snapshot-dependencies/ ./
COPY --from=layers /application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
