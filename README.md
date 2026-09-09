BT02_laptrinh_web

Ứng dụng Java Web xây dựng bằng Jakarta Servlet/JSP, JPA Hibernate, MySQL, SiteMesh 3 và Bootstrap 5.

1. Công nghệ sử dụng

Java 17+

Jakarta Servlet 6.0 / Jakarta EE 10

JSP / JSTL

JPA / Hibernate 6

MySQL

Apache Tomcat 11

Maven

SiteMesh 3 Decorator

Bootstrap 5.3.3

Bootstrap Icons 1.11.3

2. Chức năng chính

Người dùng / Authentication

Trang chủ

Đăng nhập / đăng xuất

Đăng ký tài khoản

Xác thực OTP qua email

Quên mật khẩu

Đặt lại mật khẩu

Profile người dùng

Cập nhật email, fullname, phone và avatar

Upload avatar bằng multipart/form-data

Cô lập avatar giữa tài khoản USER và ADMIN

Sản phẩm và danh mục

Xem danh sách sản phẩm

Xem chi tiết sản phẩm

Admin quản lý sản phẩm:

Thêm

Sửa

Xóa

Liệt kê

Admin quản lý danh mục:

Thêm

Sửa

Xóa

Liệt kê

Form Validation

Toàn bộ 10 form nghiệp vụ đang hoạt động có:

Client-side validation bằng HTML5

Server-side validation

Thông báo lỗi tiếng Việt

Safe input repopulation

Không repopulate password

Validation email / username / password / OTP / phone

Validation số lượng và giá sản phẩm

Validation multipart upload

Whitelist định dạng ảnh .jpg, .jpeg, .png, .gif, .webp

Giới hạn ảnh tối đa 5 MB

Kiểm tra MIME type

Không lưu file upload khi form không hợp lệ

3. SiteMesh 3 Decorator

Hệ thống sử dụng duy nhất một Bootstrap Decorator Template:

src/main/webapp/WEB-INF/decorators/default.jsp

Decorator dùng chung cho:

Guest

USER

ADMIN

Các thành phần chính:

Bootstrap 5.3.3

Bootstrap Icons

Navbar phân quyền theo session

Sticky Footer

SiteMesh <sitemesh:write property="title"/>

SiteMesh <sitemesh:write property="head"/>

SiteMesh <sitemesh:write property="body"/>

Các tài nguyên tĩnh như CSS, JS và image được loại trừ khỏi SiteMesh.

4. Các form nghiệp vụ

STT

Endpoint

Controller / Servlet

JSP

Loại form

1

/login

LoginServlet.java

login.jsp

URL-encoded

2

/register

RegisterServlet.java / AuthService.java

register.jsp

URL-encoded

3

/verify

VerifyOtpServlet.java

verify.jsp

URL-encoded

4

/forgot-password

ForgotPasswordServlet.java

forgot-password.jsp

URL-encoded

5

/reset-password

ResetPasswordServlet.java

reset-password.jsp

URL-encoded

6

/profile

ProfileServlet.java

profile.jsp

Multipart

7

/admin/products/add

ProductAddServlet.java

views/admin/add-product.jsp

Multipart

8

/admin/products/edit

ProductEditServlet.java

views/admin/edit-product.jsp

Multipart

9

/admin/category/add

CategoryAddController.java

views/admin/add-category.jsp

Multipart

10

/admin/category/edit

CategoryEditController.java

views/admin/edit-category.jsp

Multipart

5. Cấu trúc route chính

Authentication

Chức năng

Endpoint

Trang chủ

/

Đăng nhập

/login

Đăng ký

/register

Xác thực OTP

/verify

Quên mật khẩu

/forgot-password

Đặt lại mật khẩu

/reset-password

Đăng xuất

/logout

Profile

/profile

Product

Chức năng

Endpoint

Danh sách sản phẩm

/product

Chi tiết sản phẩm

/product/detail?id={id}

Admin Product

Chức năng

Endpoint

Danh sách

/admin/products

Thêm

/admin/products/add

Sửa

/admin/products/edit?id={id}

Xóa

/admin/products/delete?id={id}

Admin Category

Chức năng

Endpoint

Danh sách

/admin/category/list

Thêm

/admin/category/add

Sửa

/admin/category/edit?id={id}

Xóa

/admin/category/delete?id={id}

Các endpoint /admin/* được bảo vệ bởi cơ chế phân quyền Admin.

6. Tài khoản kiểm thử hiện tại

Các tài khoản dưới đây là dữ liệu kiểm thử trong database categorycrud hiện tại.

ADMIN

Username: admin
Password: 123456
Email: admin@example.com
Role: ADMIN
Status: Active
Fullname: Admin Live Isolation
Phone: 0987654321
Image: users/70736205-378b-42f8-a405-d7f7ddd59bab.webp

USER

Username: user
Password: 123456
Email: user_isolated@gmail.com
Role: USER
Status: Active
Fullname: User Live Isolation
Phone: 0987654321
Image: users/14070b3a-74ba-469c-9964-49be6810f92d.webp

Đây là tài khoản phục vụ kiểm thử đồ án. Không sử dụng mật khẩu 123456 cho môi trường production.

7. Database MySQL

Tên database:

categorycrud

File database đi kèm project:

categorycrud.sql

File SQL là bản dump của schema và dữ liệu tại thời điểm export. GitHub không chứa MySQL Server; người dùng khác cần cài MySQL và import file SQL vào MySQL của họ.

Kiểm tra database

SHOW DATABASES;

USE categorycrud;

SHOW TABLES;

Kiểm tra tài khoản:

SELECT id, username, role, email, fullname, phone, image
FROM users;

Kiểm tra sản phẩm:

SELECT * FROM products;

Kiểm tra danh mục:

SELECT * FROM category;

Import database từ categorycrud.sql

Sau khi cài MySQL, có thể import bằng MySQL CLI:

mysql -u root -p < categorycrud.sql

Nếu file SQL không tự tạo/chọn database, tạo database trước:

CREATE DATABASE categorycrud;
USE categorycrud;

sau đó import file SQL.

Export / cập nhật file SQL

Để xuất database hiện tại thành file SQL:

mysqldump -u root -p categorycrud > categorycrud.sql

Nếu máy không nhận lệnh mysqldump, hãy dùng đường dẫn đầy đủ tới mysqldump.exe trong thư mục bin của MySQL.

8. Cấu hình kết nối MySQL

Project cần kết nối tới MySQL của máy đang chạy ứng dụng.

Thông tin mặc định thường dùng:

Host: localhost
Port: 3306
Database: categorycrud
Username: root
Password: <password MySQL của máy hiện tại>

Không commit mật khẩu MySQL cá nhân lên GitHub. Mỗi máy có thể có username/password MySQL khác nhau.

Sau khi clone project từ GitHub, chỉ cần import categorycrud.sql vào MySQL của máy đó và cập nhật cấu hình kết nối database tương ứng.

9. Cấu hình Gmail OTP

Chức năng đăng ký và quên mật khẩu sử dụng Gmail SMTP để gửi OTP.

Cách 1 — mail.properties

Mở:

src/main/resources/mail.properties

hoặc tạo từ:

mail.properties.example

Cấu hình:

mail.host=smtp.gmail.com
mail.port=587
mail.username=your_email@gmail.com
mail.password=your_16_char_app_password
mail.from=your_email@gmail.com

mail.password phải là Google App Password, không phải mật khẩu đăng nhập Gmail thông thường.

Không commit file chứa App Password lên GitHub.

Cách 2 — VM Arguments trong Eclipse + Tomcat

Thêm:

-DMAIL_USERNAME=your_email@gmail.com -DMAIL_PASSWORD=your_16_char_app_password

Cách 3 — Environment Variables

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_16_char_app_password
MAIL_FROM=your_email@gmail.com

10. Chạy project

Build và test bằng Maven

Từ thư mục project:

mvn clean test

Build WAR:

mvn clean package

File WAR nằm trong:

target/

Deploy WAR vào Tomcat 11 và khởi động server.

Ứng dụng thường truy cập tại:

http://localhost:8080/bt02_laptrinh_web/

11. Kiểm thử

Unit Test

mvn clean test

Bộ test bao gồm validation, authentication, profile và các service/DAO liên quan.

SiteMesh Integration

Nếu repository có script:

py verify_sitemesh.py

Form Validation E2E

Nếu repository có script:

py verify_validation_e2e.py

Avatar Isolation

Nếu repository có script:

py verify_avatar_bidirectional.py

Kịch bản bắt buộc:

USER đổi avatar
→ ADMIN không đổi avatar

ADMIN đổi avatar
→ USER không đổi avatar

12. Legacy Demo Modules

Một số module Cookie/Session cũ được giữ lại để phục vụ mục đích học thuật và không tham gia luồng chính của website:

login.html
login-session.html
LoginCookieServlet
HomeCookieServlet
LoginSessionServlet
HomeSessionServlet

Các route legacy:

/login-cookie
/home-cookie
/logout-cookie

/login-session
/home-session
/logout-session

13. File upload

Các file avatar/image được lưu theo đường dẫn tương đối, ví dụ:

users/<uuid>.<ext>

Các định dạng ảnh được chấp nhận:

.jpg
.jpeg
.png
.gif
.webp

Giới hạn kích thước:

5 MB

Khi chuyển project sang máy khác, cần bảo đảm thư mục upload được cấu hình đúng trong Constant.DIR và ứng dụng có quyền ghi.

Không nên đưa các file upload cá nhân hoặc dữ liệu test không cần thiết vào repository nếu không phục vụ bài tập.

14. Form Validation

Hệ thống áp dụng validation ở cả hai tầng.

Client-side

Sử dụng HTML5 và Bootstrap 5:

required
minlength
maxlength
pattern
min
max
step
accept

Server-side

Sử dụng:

src/main/java/vn/iotstar/util/ValidationUtil.java

Các nhóm kiểm tra chính:

Username 3–30 ký tự, chữ/số/gạch dưới

Password 8–100 ký tự

Email hợp lệ

OTP đúng 6 chữ số

Phone Việt Nam 10–11 chữ số bắt đầu bằng 0

Giá sản phẩm không âm

Số lượng là số nguyên không âm

Category ID phải hợp lệ và tồn tại

Image extension / MIME / size

Control characters

Khi validation thất bại:

Không cập nhật database

Không lưu file upload không hợp lệ

Giữ lại dữ liệu an toàn đã nhập

Không repopulate password

Hiển thị thông báo lỗi tiếng Việt

15. Git và GitHub

Trước khi commit:

git status
git diff

Sau khi kiểm tra:

git add .
git commit -m "Complete SiteMesh integration and form validation"
git push

Không commit:

Mật khẩu MySQL

Gmail App Password

API keys

Secret tokens

File cấu hình chứa secret

Dữ liệu cá nhân không cần thiết

16. Chạy project sau khi clone GitHub

Quy trình chuẩn cho máy khác:

1. Clone repository
2. Cài JDK 17+
3. Cài Maven
4. Cài MySQL
5. Tạo/import database từ categorycrud.sql
6. Cấu hình username/password MySQL trên máy hiện tại
7. Cấu hình Gmail OTP nếu cần dùng đăng ký/quên mật khẩu
8. Chạy mvn clean package
9. Deploy WAR lên Tomcat 11
10. Mở http://localhost:8080/bt02_laptrinh_web/

Lưu ý: clone GitHub không tự cài MySQL hoặc tự tạo database. File categorycrud.sql cung cấp schema + dữ liệu để người dùng import vào MySQL của họ.

17. Trạng thái Assignment 03

Các phần chính đã hoàn thiện:

SiteMesh 3 Decorator

Một Bootstrap template duy nhất

Guest / USER / ADMIN navigation

Product CRUD

Category CRUD

User Profile

Multipart upload

User/Admin avatar isolation

Form validation Client-side

Form validation Server-side

Bootstrap validation UI

JPA / Hibernate persistence

Authentication / Authorization

OTP / Password Reset

Kết quả kiểm thử gần nhất:

Maven Unit Tests: 45/45 PASS
Form Validation E2E: 49/49 PASS
SiteMesh Integration: 15/15 PASS
Bidirectional Avatar Isolation: PASS