<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<title>Quên mật khẩu - MyShop</title>

<div class="row justify-content-center my-4">
    <div class="col-md-5 col-lg-4">
        <div class="card shadow-sm border-0">
            <div class="card-body p-4">
                <h3 class="card-title text-center mb-4 fw-bold">
                    <i class="bi bi-question-circle text-warning me-2"></i>Quên mật khẩu
                </h3>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger d-flex align-items-center mb-3" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        <div>${error}</div>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/forgot-password" method="post">
                    <div class="mb-3">
                        <label for="email" class="form-label fw-semibold">Nhập Email đã đăng ký:</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                            <input type="email" class="form-control" id="email" name="email" 
                                   value="${not empty email ? email : ''}"
                                   maxlength="100" required autofocus placeholder="example@domain.com">
                        </div>
                    </div>

                    <button type="submit" class="btn btn-warning w-100 py-2 fw-bold mb-3">
                        <i class="bi bi-send me-1"></i>Gửi mã OTP đặt lại mật khẩu
                    </button>
                </form>

                <div class="text-center pt-2 border-top">
                    <p class="mb-0">
                        <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">
                            <i class="bi bi-arrow-left me-1"></i>Quay lại Đăng nhập
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

