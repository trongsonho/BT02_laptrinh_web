<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<title>Hồ sơ cá nhân - MyShop</title>

<style>
    .profile-card {
        max-width: 650px;
        margin: 20px auto;
        background: #ffffff;
        border-radius: 10px;
        padding: 35px 40px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
        border: 1px solid #dee2e6;
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
        border: 4px solid #0d6efd;
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
    .file-hint {
        font-size: 12px;
        color: #6c757d;
        margin-top: 6px;
    }
</style>

<div class="profile-card">
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
        <div class="alert alert-success d-flex align-items-center mb-4" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i>
            <div>${message}</div>
        </div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-danger d-flex align-items-center mb-4" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>
            <div>${error}</div>
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/profile" enctype="multipart/form-data">
        <div class="mb-3">
            <label class="form-label fw-bold">Username:</label>
            <input type="text" class="form-control bg-light" value="${user.username}" readonly />
        </div>

        <div class="mb-3">
            <label for="email" class="form-label fw-bold">Email:</label>
            <input type="email" id="email" name="email" class="form-control"
                   value="${user.email}" placeholder="Nhập địa chỉ email..." required />
        </div>

        <div class="mb-3">
            <label for="fullname" class="form-label fw-bold">Họ và tên:</label>
            <input type="text" id="fullname" name="fullname" class="form-control"
                   value="${user.fullname}" placeholder="Nhập họ và tên..." required />
        </div>

        <div class="mb-3">
            <label for="phone" class="form-label fw-bold">Số điện thoại:</label>
            <input type="text" id="phone" name="phone" class="form-control"
                   value="${user.phone}" placeholder="Nhập số điện thoại (ví dụ: 0901234567)..." required />
        </div>

        <div class="mb-3">
            <label for="image" class="form-label fw-bold">Ảnh đại diện mới:</label>
            <input type="file" id="image" name="image" class="form-control" accept="image/*" />
            <div class="file-hint">Định dạng hỗ trợ: .jpg, .jpeg, .png, .gif, .webp (Tối đa 5MB). Để trống nếu không muốn đổi ảnh.</div>
        </div>

        <button type="submit" class="btn btn-primary w-100 py-2 fw-bold mt-2">
            <i class="bi bi-save me-1"></i>Cập nhật
        </button>
    </form>
</div>

