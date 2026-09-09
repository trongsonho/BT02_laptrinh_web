<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<title>Đăng nhập - MyShop</title>

<div class="row justify-content-center my-4">
    <div class="col-md-5 col-lg-4">
        <div class="card shadow-sm border-0">
            <div class="card-body p-4">
                <h3 class="card-title text-center mb-4 fw-bold">
                    <i class="bi bi-box-arrow-in-right text-primary me-2"></i>Đăng nhập
                </h3>

                <c:if test="${not empty message}">
                    <div class="alert alert-success d-flex align-items-center mb-3" role="alert">
                        <i class="bi bi-check-circle-fill me-2"></i>
                        <div>${message}</div>
                    </div>
                </c:if>
                <c:if test="${not empty sessionScope.message}">
                    <div class="alert alert-success d-flex align-items-center mb-3" role="alert">
                        <i class="bi bi-check-circle-fill me-2"></i>
                        <div>${sessionScope.message}</div>
                    </div>
                    <c:remove var="message" scope="session" />
                </c:if>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger d-flex align-items-center mb-3" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        <div>${error}</div>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="mb-3">
                        <label for="username" class="form-label fw-semibold">Tên đăng nhập:</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-person"></i></span>
                            <input type="text" class="form-control" id="username" name="username" 
                                   value="${not empty username ? username : ''}" 
                                   maxlength="50" required autofocus placeholder="Nhập tên đăng nhập...">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label fw-semibold">Mật khẩu:</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-lock"></i></span>
                            <input type="password" class="form-control" id="password" name="password" 
                                   maxlength="100" required placeholder="Nhập mật khẩu...">
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary w-100 py-2 fw-bold mb-3">
                        Đăng nhập
                    </button>
                </form>

                <div class="text-center pt-2 border-top">
                    <p class="mb-2">
                        <a href="${pageContext.request.contextPath}/register" class="text-decoration-none fw-semibold">Đăng ký tài khoản</a> &bull; 
                        <a href="${pageContext.request.contextPath}/forgot-password" class="text-decoration-none text-muted">Quên mật khẩu?</a>
                    </p>
                    <p class="mb-0">
                        <a href="${pageContext.request.contextPath}/" class="text-decoration-none text-secondary">
                            <i class="bi bi-arrow-left me-1"></i>Về trang chủ
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

