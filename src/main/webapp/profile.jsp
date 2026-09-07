<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hồ sơ cá nhân - MyShop</title>
    <style>
        .profile-container {
            max-width: 650px;
            margin: 40px auto;
            background: #ffffff;
            border-radius: 10px;
            padding: 35px 40px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
        }
        .profile-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .profile-header h2 {
            margin: 10px 0 0 0;
            color: #2c3e50;
            font-size: 26px;
        }
        .avatar-wrapper {
            width: 130px;
            height: 130px;
            margin: 0 auto 15px auto;
            border-radius: 50%;
            overflow: hidden;
            border: 4px solid #007bff;
            background: #f1f3f5;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 2px 8px rgba(0,0,0,0.15);
        }
        .avatar-wrapper img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        .alert {
            padding: 12px 18px;
            border-radius: 6px;
            margin-bottom: 25px;
            font-size: 14px;
            font-weight: 500;
        }
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            font-weight: bold;
            color: #495057;
            margin-bottom: 8px;
            font-size: 14px;
        }
        .form-control-static {
            padding: 10px 14px;
            background-color: #e9ecef;
            border: 1px solid #ced4da;
            border-radius: 6px;
            color: #495057;
            font-size: 15px;
            font-weight: 500;
        }
        .form-control {
            width: 100%;
            padding: 10px 14px;
            border: 1px solid #ced4da;
            border-radius: 6px;
            font-size: 15px;
            box-sizing: border-box;
            transition: border-color 0.2s, box-shadow 0.2s;
        }
        .form-control:focus {
            outline: none;
            border-color: #80bdff;
            box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);
        }
        .file-hint {
            font-size: 12px;
            color: #6c757d;
            margin-top: 6px;
        }
        .btn-submit {
            width: 100%;
            background-color: #007bff;
            color: white;
            padding: 12px 20px;
            font-size: 16px;
            font-weight: bold;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.2s;
            margin-top: 10px;
        }
        .btn-submit:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<div class="profile-container">
    <div class="profile-header">
        <div class="avatar-wrapper">
            <c:choose>
                <c:when test="${not empty user.image}">
                    <c:choose>
                        <c:when test="${user.image.startsWith('http')}">
                            <img src="${user.image}" alt="${user.username}" />
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/image?fname=${user.image}" alt="${user.username}" />
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <img src="https://via.placeholder.com/150?text=Avatar" alt="Avatar mặc định" />
                </c:otherwise>
            </c:choose>
        </div>
        <h2>Hồ sơ cá nhân</h2>
    </div>

    <c:if test="${not empty message}">
        <div class="alert alert-success">${message}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/profile" enctype="multipart/form-data">
        <div class="form-group">
            <label>Username:</label>
            <div class="form-control-static">${user.username}</div>
        </div>

        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" class="form-control"
                   value="${user.email}" placeholder="Nhập địa chỉ email..." required />
        </div>

        <div class="form-group">
            <label for="fullname">Họ và tên:</label>
            <input type="text" id="fullname" name="fullname" class="form-control"
                   value="${user.fullname}" placeholder="Nhập họ và tên..." required />
        </div>

        <div class="form-group">
            <label for="phone">Số điện thoại:</label>
            <input type="text" id="phone" name="phone" class="form-control"
                   value="${user.phone}" placeholder="Nhập số điện thoại (ví dụ: 0901234567)..." required />
        </div>

        <div class="form-group">
            <label for="image">Ảnh đại diện mới:</label>
            <input type="file" id="image" name="image" class="form-control" accept="image/*" />
            <div class="file-hint">Định dạng hỗ trợ: .jpg, .jpeg, .png, .gif, .webp (Tối đa 5MB). Để trống nếu không muốn đổi ảnh.</div>
        </div>

        <button type="submit" class="btn-submit">Cập nhật</button>
    </form>
</div>

</body>
</html>
