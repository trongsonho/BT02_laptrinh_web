<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<title>Đăng ký tài khoản - MyShop</title>

<div class="row justify-content-center my-4">
    <div class="col-md-5 col-lg-4">
        <div class="card shadow-sm border-0">
            <div class="card-body p-4">
                <h3 class="card-title text-center mb-4 fw-bold">
                    <i class="bi bi-person-plus text-primary me-2"></i>Đăng ký tài khoản
                </h3>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger d-flex align-items-center mb-3" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        <div>${error}</div>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/register" method="post">
                    <div class="mb-3">
                        <label for="username" class="form-label fw-semibold">Tên đăng nhập:</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-person"></i></span>
                            <input type="text" class="form-control" id="username" name="username" 
                                   value="${not empty username ? username : ''}" 
                                   autocomplete="off" 
                                   minlength="3" maxlength="30" pattern="[a-zA-Z0-9_]{3,30}"
                                   title="Tên đăng nhập gồm 3-30 ký tự, chỉ chứa chữ cái, chữ số và gạch dưới (_)"
                                   placeholder="Nhập tên đăng nhập (3-30 ký tự)..." required autofocus>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label fw-semibold">Mật khẩu:</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-lock"></i></span>
                            <input type="password" class="form-control" id="password" name="password" 
                                   value="" 
                                   autocomplete="new-password" 
                                   minlength="8" maxlength="100"
                                   title="Mật khẩu phải có từ 8 đến 100 ký tự"
                                   placeholder="Nhập mật khẩu (tối thiểu 8 ký tự)..." required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label fw-semibold">Email (*):</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                            <input type="email" class="form-control" id="email" name="email" 
                                   value="${not empty email ? email : ''}" 
                                   autocomplete="email"
                                   maxlength="100"
                                   placeholder="Nhập email để nhận mã OTP..." required>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-warning w-100 py-2 fw-bold mb-3">
                        Đăng ký
                    </button>
                </form>

                <div class="text-center pt-2 border-top">
                    <p class="mb-0">
                        Đã có tài khoản? 
                        <a href="${pageContext.request.contextPath}/login" class="text-decoration-none fw-semibold">
                            Đăng nhập ngay
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

