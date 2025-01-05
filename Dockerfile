FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/newsly-0.0.1-SNAPSHOT.jar newsly.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","ai-todo.jar"]