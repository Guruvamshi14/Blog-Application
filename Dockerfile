# Stage 1: Build the app using Maven
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the app using JDK
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port Spring Boot will run on
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]
