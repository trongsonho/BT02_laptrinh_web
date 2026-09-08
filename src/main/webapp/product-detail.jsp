<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<title>${product.name} - Chi tiết sản phẩm</title>

<style>
    .detail-card { max-width: 1000px; margin: 20px auto; padding: 30px; background: white; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.08); }
    .breadcrumb a { color: #0d6efd; text-decoration: none; }
    .detail-wrapper { display: flex; gap: 40px; flex-wrap: wrap; }
    .detail-image { flex: 1; min-width: 300px; max-width: 450px; }
    .detail-image img { width: 100%; border-radius: 8px; border: 1px solid #dee2e6; }
    .detail-info { flex: 1; min-width: 300px; }
    .detail-name { font-size: 26px; font-weight: bold; margin-bottom: 15px; color: #222; }
    .detail-price { font-size: 24px; color: #e44d26; font-weight: bold; margin-bottom: 15px; }
    .detail-meta { margin-bottom: 15px; line-height: 1.8; color: #555; }
    .detail-meta strong { color: #333; }
    .detail-description { margin-top: 25px; border-top: 1px solid #dee2e6; padding-top: 20px; line-height: 1.6; }
</style>

<nav aria-label="breadcrumb" class="mb-3">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Trang chủ</a></li>
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/product">Sản phẩm</a></li>
        <li class="breadcrumb-item active" aria-current="page">${product.name}</li>
    </ol>
</nav>

<div class="detail-card">
    <div class="detail-wrapper">
        <div class="detail-image">
            <c:choose>
                <c:when test="${not empty product.image}">
                    <c:choose>
                        <c:when test="${product.image.startsWith('http')}">
                            <img src="${product.image}" alt="${product.name}">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/image?fname=${product.image}" alt="${product.name}">
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <img src="https://via.placeholder.com/450x350?text=No+Image" alt="${product.name}">
                </c:otherwise>
            </c:choose>
        </div>

        <div class="detail-info">
            <div class="detail-name">${product.name}</div>
            <div class="detail-price">
                <fmt:formatNumber value="${product.price}" pattern="#,##0" /> VNĐ
            </div>
            <div class="detail-meta">
                <p><strong>Mã sản phẩm:</strong> #${product.id}</p>
                <p><strong>Số lượng còn:</strong> ${product.quantity}</p>
                <p><strong>Danh mục:</strong> 
                    <c:choose>
                        <c:when test="${not empty product.category}">
                            ${product.category.name}
                        </c:when>
                        <c:otherwise>Chưa phân loại</c:otherwise>
                    </c:choose>
                </p>
                <p><strong>Ngày tạo:</strong> ${product.createdAt}</p>
            </div>

            <div class="detail-description">
                <h5>Mô tả sản phẩm</h5>
                <p class="text-muted">${not empty product.description ? product.description : 'Chưa có mô tả cho sản phẩm này.'}</p>
            </div>

            <a href="${pageContext.request.contextPath}/product" class="btn btn-secondary mt-3">
                <i class="bi bi-arrow-left me-1"></i>Quay lại danh sách
            </a>
        </div>
    </div>
</div>

