FROM openjdk:17-jdk-slim

# Tạo thư mục làm việc
WORKDIR /app

# Copy file Maven wrapper và pom.xml để build
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Build dự án (skip tests để nhanh)
RUN ./mvnw clean package -DskipTests

# Copy file JAR đã build
COPY target/*.jar app.jar

# Expose port 8080 (mặc định Spring Boot)
EXPOSE 8080

# Chạy app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
