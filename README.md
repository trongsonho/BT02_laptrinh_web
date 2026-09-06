# BT02_laptrinh_web

Java Web Jakarta EE 10 Servlet / JSP / JPA Hibernate / MySQL application.

## Test Credentials (Default Account)

A default testing account is automatically initialized on application startup if it does not already exist:

- **Username:** `admin`
- **Password:** `123456`
- **Email:** `admin@example.com`
- **Status:** Active (pre-activated, usable immediately without OTP verification)
- **Role:** ADMIN
- **Username:** `user`
- **Password:** `123456`
- **Email:** `user@example.com`
- **Status:** Active (pre-activated, usable immediately without OTP verification)
- **Role:** USER
## Authentication Flow

- **Trang chủ:** `/` -> `HomeServlet` -> `home.jsp` (hiển thị 10 sản phẩm mới nhất)
- **Đăng nhập:** `/login` -> `LoginServlet` -> `login.jsp`
- **Đăng ký:** `/register` -> `RegisterServlet` -> `register.jsp` -> gửi mã OTP qua email
- **Xác thực OTP:** `/verify` -> `VerifyOtpServlet` -> `verify.jsp` -> kích hoạt tài khoản (`active = true`)
- **Quên mật khẩu:** `/forgot-password` -> `ForgotPasswordServlet` -> `forgot-password.jsp`
- **Đặt lại mật khẩu:** `/reset-password` -> `ResetPasswordServlet` -> `reset-password.jsp`
- **Đăng xuất:** `/logout` -> `LogoutServlet` (hủy session và chuyển hướng về `/login`)

## Product & Category Management

- **Danh sách sản phẩm (khách hàng):** `/product` (phân trang 6 sản phẩm/trang)
- **Chi tiết sản phẩm:** `/product/detail?id={id}`
- **Quản lý sản phẩm (Admin):** `/admin/products`
  - Thêm: `/admin/products/add`
  - Sửa: `/admin/products/edit?id={id}`
  - Xóa: `/admin/products/delete?id={id}`
- **Quản lý danh mục (Admin):** `/admin/category/list`
  - Thêm: `/admin/category/add`
  - Sửa: `/admin/category/edit?id={id}`
  - Xóa: `/admin/category/delete?id={id}`

## Legacy Demo Modules

Các file demo bài tập Cookie/Session cũ được giữ lại phục vụ mục đích tham khảo học thuật (không tham gia luồng chính của website):
- `login.html` -> `/login-cookie` (`LoginCookieServlet`) -> `/home-cookie` (`HomeCookieServlet`) -> `/logout-cookie`
- `login-session.html` -> `/login-session` (`LoginSessionServlet`) -> `/home-session` (`HomeSessionServlet`) -> `/logout-session`

## Cấu hình gửi Email OTP (Gmail SMTP)

Chức năng Đăng ký tài khoản và Quên mật khẩu gửi mã xác thực OTP 6 số qua email. Để gửi được thư, sinh viên cần cấu hình tài khoản gửi thư theo một trong các cách sau:

### Cách 1: Sử dụng file `mail.properties` (Khuyên dùng khi chạy trong Eclipse)
1. Mở file `src/main/resources/mail.properties` (hoặc copy từ `mail.properties.example`).
2. Điền thông tin tài khoản Gmail của bạn:
   ```properties
   mail.host=smtp.gmail.com
   mail.port=587
   mail.username=your_email@gmail.com
   mail.password=your_16_char_app_password
   mail.from=your_email@gmail.com
   ```
   > **LƯU Ý QUAN TRỌNG VỀ GMAIL**: `mail.password` **PHẢI LÀ MẬT KHẨU ỨNG DỤNG** (App Password gồm 16 ký tự, ví dụ `abcd efgh ijkl mnop`), **KHÔNG ĐƯỢC** dùng mật khẩu đăng nhập tài khoản Gmail thông thường. Để lấy App Password:
   > - Bật Xác minh 2 bước (2-Step Verification) trên tài khoản Google.
   > - Vào `Quản lý Tài khoản Google` -> mục `Bảo mật` -> `Mật khẩu ứng dụng` -> Tạo mật khẩu ứng dụng cho "Thư" -> Copy mã 16 ký tự.
   > - File `mail.properties` đã được cấu hình trong `.gitignore` để không bị commit lên Git.

### Cách 2: Cấu hình VM Arguments trong Eclipse + Tomcat
1. Trong Eclipse, mở menu **Run** -> **Run Configurations...** (hoặc double click vào Server Tomcat trong tab Servers -> chọn **Open launch configuration**).
2. Chuyển sang tab **Arguments**.
3. Tại ô **VM arguments**, thêm vào cuối:
   ```text
   -DMAIL_USERNAME=your_email@gmail.com -DMAIL_PASSWORD=your_16_char_app_password
   ```
4. Nhấn **Apply** và khởi động lại Tomcat.

### Cách 3: Biến môi trường hệ điều hành (Environment Variables)
- `MAIL_HOST` (Mặc định: `smtp.gmail.com`)
- `MAIL_PORT` (Mặc định: `587`)
- `MAIL_USERNAME`: Địa chỉ email Gmail gửi thư.
- `MAIL_PASSWORD`: Mật khẩu ứng dụng (App Password 16 ký tự).
- `MAIL_FROM`: Địa chỉ email người gửi (nếu không set sẽ lấy theo `MAIL_USERNAME`).
