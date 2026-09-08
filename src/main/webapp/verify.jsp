<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<title>Xác thực OTP - MyShop</title>

<div class="row justify-content-center my-4">
    <div class="col-md-5 col-lg-4">
        <div class="card shadow-sm border-0">
            <div class="card-body p-4">
                <h3 class="card-title text-center mb-3 fw-bold">
                    <i class="bi bi-shield-check text-success me-2"></i>Xác thực OTP
                </h3>

                <div class="alert alert-info d-flex align-items-center mb-3" role="alert">
                    <i class="bi bi-info-circle-fill me-2 flex-shrink-0"></i>
                    <div>Mã OTP đã được gửi đến email của bạn. Vui lòng nhập mã OTP để kích hoạt tài khoản (hiệu lực 5 phút).</div>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger d-flex align-items-center mb-3" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        <div>${error}</div>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/verify" method="post">
                    <div class="mb-3">
                        <label for="email" class="form-label fw-semibold">Email:</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                            <input type="email" class="form-control ${not empty email || not empty sessionScope.verifyEmail ? 'bg-light' : ''}" 
                                   id="email" name="email" 
                                   value="${not empty email ? email : sessionScope.verifyEmail}" 
                                   ${not empty email || not empty sessionScope.verifyEmail ? 'readonly' : ''} required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="otp" class="form-label fw-semibold">Mã OTP (6 chữ số):</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-key"></i></span>
                            <input type="text" class="form-control text-center fw-bold fs-5 letter-spacing" 
                                   id="otp" name="otp" maxlength="6" 
                                   placeholder="000000" required autofocus>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-success w-100 py-2 fw-bold mb-3">
                        <i class="bi bi-check2-circle me-1"></i>Kích hoạt tài khoản
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

