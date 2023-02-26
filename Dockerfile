# Stage 1: Build the application with Maven
FROM maven:3.8.4-openjdk-11 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Stage 2: Build the application image
FROM openjdk:11-jre-slim
COPY --from=build /app/target/btrack.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
