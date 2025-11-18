FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy Maven files để build
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Cấp quyền thực thi cho mvnw (fix lỗi 126)
RUN chmod +x mvnw

# Build JAR (skip tests cho nhanh)
RUN ./mvnw clean package -DskipTests

# Copy JAR đã build (nếu build thành công)
RUN mkdir -p target
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
