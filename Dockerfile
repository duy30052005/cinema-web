# ==========================
# Stage 1: Build Maven Project
# ==========================
FROM maven:3.9.9-jdk-21 AS build

WORKDIR /app

# Copy pom.xml và source code
COPY pom.xml .
COPY src ./src

# Build project, skip tests để nhanh hơn
RUN mvn clean package -DskipTests

# ==========================
# Stage 2: Run Spring Boot JAR
# ==========================
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy JAR từ stage build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
