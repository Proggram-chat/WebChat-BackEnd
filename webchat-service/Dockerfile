FROM openjdk:21-slim AS build
WORKDIR /app

COPY pom.xml .
COPY webchat-service/ service/
RUN cd service && chmod +x ./mvnw && ./mvnw clean package -DskipTests -DskipITs

FROM openjdk:21-slim AS builder
WORKDIR /app
COPY --from=build /app/service/target/webchat-service-0.0.1-SNAPSHOT.jar /app/webchat-service.jar
RUN java -Djarmode=layertools -jar webchat-service.jar extract

FROM openjdk:21-slim
RUN apt-get update && apt-get install -y wget
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./

EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "org.springframework.boot.loader.launch.JarLauncher"]