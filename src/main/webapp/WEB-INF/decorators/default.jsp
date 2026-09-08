<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title">MyShop - Cửa hàng trực tuyến</sitemesh:write></title>

    <!-- Bootstrap 5 CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">

    <!-- SiteMesh Head extension point -->
    <sitemesh:write property="head"/>

    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background-color: #f8f9fa;
            color: #333;
        }
        main.site-main-content {
            flex: 1 0 auto;
        }
        .footer {
            flex-shrink: 0;
        }
        .navbar-brand {
            font-weight: 700;
            letter-spacing: 0.5px;
        }
    </style>
</head>
<body>

    <!-- Shared Bootstrap 5 Navbar with Role-based Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
        <div class="container">
            <a class="navbar-brand text-warning" href="${pageContext.request.contextPath}/">
                <i class="bi bi-shop me-1"></i>MyShop
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar"
                    aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="mainNavbar">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/">
                            <i class="bi bi-house-door me-1"></i>Trang chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/product">
                            <i class="bi bi-grid me-1"></i>Sản phẩm
                        </a>
                    </li>

                    <%-- Admin-specific navigation links --%>
                    <c:if test="${not empty sessionScope.user and (sessionScope.user.role == 'ADMIN' or sessionScope.user.role == 'admin')}">
                        <li class="nav-item">
                            <a class="nav-link text-info" href="${pageContext.request.contextPath}/admin/products">
                                <i class="bi bi-box-seam me-1"></i>Admin Sản phẩm
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-info" href="${pageContext.request.contextPath}/admin/category/list">
                                <i class="bi bi-tags me-1"></i>Admin Category
                            </a>
                        </li>
                    </c:if>
                </ul>

                <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-lg-center">
                    <c:choose>
                        <%-- Case: Logged-in User (Admin or Normal User) --%>
                        <c:when test="${not empty sessionScope.user}">
                            <li class="nav-item me-2">
                                <a class="nav-link" href="${pageContext.request.contextPath}/profile">
                                    <i class="bi bi-person-circle me-1"></i>Hồ sơ
                                </a>
                            </li>
                            <li class="nav-item me-3">
                                <span class="navbar-text text-warning fw-semibold">
                                    Chào, ${sessionScope.user.username}
                                </span>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout">
                                    <i class="bi bi-box-arrow-right me-1"></i>Đăng xuất
                                </a>
                            </li>
                        </c:when>

                        <%-- Case: Guest (Not logged in) --%>
                        <c:otherwise>
                            <li class="nav-item me-2">
                                <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/login">
                                    <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-warning btn-sm fw-semibold" href="${pageContext.request.contextPath}/register">
                                    <i class="bi bi-person-plus me-1"></i>Đăng ký
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <!-- SiteMesh Managed Main Body -->
    <main class="site-main-content py-4">
        <div class="container">
            <sitemesh:write property="body"/>
        </div>
    </main>

    <!-- Shared Bootstrap 5 Footer -->
    <footer class="footer bg-dark text-white py-4 mt-auto border-top border-secondary">
        <div class="container text-center">
            <p class="mb-1 fw-semibold">&copy; 2026 MyShop. All rights reserved.</p>
            <small class="text-secondary">Hệ thống Mua sắm &amp; Quản lý Người dùng | Assignment 03</small>
        </div>
    </footer>

    <!-- Bootstrap 5 JS Bundle CDN -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
