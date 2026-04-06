# 🎬 Cinema Booking System - Backend API

## 📝 Giới thiệu
Dự án cung cấp hệ thống Backend RESTful API mạnh mẽ cho nền tảng đặt vé xem phim trực tuyến. Hệ thống được thiết kế theo kiến trúc phân tầng chuẩn (Controller - Service - Repository), đảm bảo hiệu năng cao, bảo mật chặt chẽ và được đóng gói Docker để vận hành linh hoạt trên môi trường Cloud.

## 🚀 Công nghệ sử dụng
* **Ngôn ngữ:** Java 21
* **Framework chính:** Spring Boot 3.4.5
* **Cơ sở dữ liệu:** PostgreSQL & Spring Data JPA
* **Bảo mật:** Spring Security, OAuth2 Resource Server (JWT)
* **Mapping & Tiện ích:** MapStruct, Lombok
* **Email Service:** Spring Boot Mail, Resend API
* **DevOps & Cloud:** Docker (Multi-stage build), Render PaaS
* **Monitoring & Logging:** Spring Boot Actuator, Zalando Logbook

## 💡 Các tính năng cốt lõi (Key Features)

### 1. Kiến trúc Hệ thống & Quản lý Dữ liệu
* Triển khai kiến trúc **N-Tier (Controller - Service - Repository - Entity)** giúp tách biệt logic nghiệp vụ và tối ưu hóa việc bảo trì.
* Tương tác CSDL PostgreSQL thông qua **Spring Data JPA** / Hibernate, sử dụng Entity để map với các bảng dữ liệu thực tế.
* Tự động hóa quá trình chuyển đổi giữa DTO (Data Transfer Object) và Entity bằng **MapStruct**, giúp ẩn giấu cấu trúc database thật và tăng tính bảo mật cho API.

### 2. Xác thực & Phân quyền (Authentication & Authorization)
* Triển khai hệ thống bảo mật bằng **Spring Security** kết hợp **OAuth2** (JWT - JSON Web Token).
* Phân quyền truy cập rõ ràng giữa các Role: `ADMIN` (quản lý rạp, lịch chiếu, phim) và `USER` (đặt vé, xem lịch sử).
* Mã hóa mật khẩu an toàn.

### 3. Dịch vụ Thông báo (Notification Service)
* Tích hợp **Resend API** và **JavaMailSender** để xử lý hàng đợi gửi email.
* Tự động gửi email xác nhận đặt vé, gửi mã OTP hoặc thông báo đăng ký tài khoản thành công cho khách hàng.

### 4. Quản lý & Theo dõi (Monitoring)
* Sử dụng **Spring Boot Actuator** để theo dõi "sức khỏe" (health checks) của ứng dụng.
* Sử dụng **Zalando Logbook** để ghi log chi tiết các HTTP request/response, hỗ trợ đắc lực cho quá trình gỡ lỗi (debugging).

### 5. Triển khai Cloud (Docker & Render)
* Giải quyết hạn chế môi trường Java native trên nền tảng Cloud (Render) bằng cách đóng gói ứng dụng với **Docker**.
* Sử dụng chiến lược **Multi-stage build**: Tách biệt môi trường `maven:alpine` (để build mã nguồn) và môi trường `eclipse-temurin:21-jdk` (để chạy ứng dụng), giúp tối ưu hóa dung lượng image và bảo mật mã nguồn.

## 📖 Tài liệu API (API Documentation)
Hệ thống cung cấp tài liệu API chi tiết theo chuẩn **OpenAPI 3.1**, toàn bộ đặc tả được định nghĩa tại file `open-api.yaml` ở thư mục gốc của dự án.

Để xem chi tiết các Endpoints (GET, POST, PUT, DELETE) và cấu trúc dữ liệu (Schemas), bạn có thể sử dụng một trong hai cách sau:

**Cách 1: Xem giao diện trực quan trực tuyến (Swagger Editor)**
1. Truy cập trang web [Swagger Editor](https://editor.swagger.io/).
2. Copy toàn bộ nội dung trong file `open-api.yaml` của repository này.
3. Dán vào khung soạn thảo bên trái trang web. Giao diện tài liệu API tương tác sẽ tự động được render ở khung bên phải.

**Cách 2: Import vào Postman (Dùng để test API)**
1. Mở ứng dụng Postman trên máy của bạn.
2. Nhấn nút **Import** -> Chọn tab **File** và tải file `open-api.yaml` lên (hoặc dán trực tiếp Raw link của file từ GitHub).
3. Postman sẽ tự động sinh ra một Collection hoàn chỉnh chứa toàn bộ các API đã được cấu hình sẵn tham số (Params/Body), giúp bạn dễ dàng gửi request test hệ thống.

## 📂 Cấu trúc mã nguồn cơ bản
```text
src/main/java/com/example/demo
├── config/         # Cấu hình Security, CORS
├── controller/     # Tiếp nhận các HTTP Request (REST API)
├── dto/            # Data Transfer Objects & MapStruct Mappers
├── entity/         # Các lớp mô hình hóa cơ sở dữ liệu (PostgreSQL)
├── repository/     # Tương tác với Database (Spring Data JPA)
├── service/        # Chứa logic nghiệp vụ lõi
└── DemoApplication.java

Dockerfile          # Script build Docker image (Multi-stage)
open-api.yaml       # Đặc tả tài liệu hệ thống API
```

## 🛠 Hướng dẫn Triển khai (Local Setup)

### Bước 1: Yêu cầu hệ thống
* JDK 21
* Maven 3.x
* PostgreSQL 14+
* Docker (Tùy chọn, nếu muốn chạy qua container)
* IDE hỗ trợ Lombok (IntelliJ IDEA, Eclipse, Azure Data Studio để query DB).

### Bước 2: Cấu hình Cơ sở dữ liệu & Biến môi trường
Tạo database trên PostgreSQL và cập nhật thông tin vào file `src/main/resources/application.properties` (hoặc `.yml`):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ten_database
spring.datasource.username=postgres
spring.datasource.password=mat_khau_cua_ban
spring.jpa.hibernate.ddl-auto=update

# Cấu hình Resend / Mail (Nếu có)
resend.api.key=your_resend_api_key
```

### Bước 3: Khởi chạy dự án
**Cách 1: Chạy thuần bằng Maven**
```bash
mvn clean install
mvn spring-boot:run
```

**Cách 2: Chạy bằng Docker (Giả lập môi trường Render)**
```bash
docker build -t cinema-backend .
docker run -p 8080:8080 cinema-backend
```

Server sẽ khởi chạy mặc định tại: `http://localhost:8080`

## 👥 Tác giả
* **Huỳnh Bá Duy** - Backend Developer (Java/Spring Boot)
