<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<title>Trang chủ - MyShop</title>

<style>
    .section-title { font-size: 24px; margin-bottom: 20px; border-bottom: 2px solid #0d6efd; padding-bottom: 10px; display: flex; justify-content: space-between; align-items: center; }
    .section-title a { font-size: 16px; color: #0d6efd; text-decoration: none; }
    .product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 20px; }
    .product-card { background: white; border: 1px solid #dee2e6; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 5px rgba(0,0,0,0.05); transition: transform 0.2s, box-shadow 0.2s; display: flex; flex-direction: column; }
    .product-card:hover { transform: translateY(-5px); box-shadow: 0 5px 15px rgba(0,0,0,0.1); }
    .product-image { width: 100%; height: 180px; object-fit: cover; background-color: #eee; }
    .product-info { padding: 15px; flex-grow: 1; display: flex; flex-direction: column; }
    .product-name { font-size: 16px; font-weight: bold; margin: 0 0 10px 0; color: #333; text-decoration: none; }
    .product-name:hover { color: #0d6efd; }
    .product-price { font-size: 18px; color: #e44d26; font-weight: bold; margin-bottom: 5px; }
    .product-quantity { font-size: 13px; color: #6c757d; margin-bottom: 10px; }
    .product-action { margin-top: auto; }
    .btn-detail { display: block; text-align: center; background-color: #0d6efd; color: white; padding: 8px 12px; border-radius: 4px; text-decoration: none; font-size: 14px; font-weight: 500; }
    .btn-detail:hover { background-color: #0b5ed7; color: white; }
    .no-products { text-align: center; color: #777; font-size: 18px; padding: 50px 0; grid-column: 1 / -1; }
</style>

<div class="section-title">
    <span class="fw-bold"><i class="bi bi-fire text-danger me-2"></i>10 Sản phẩm mới nhất</span>
    <a href="${pageContext.request.contextPath}/product">Xem tất cả sản phẩm &raquo;</a>
</div>

<div class="product-grid">
    <c:choose>
        <c:when test="${not empty products}">
            <c:forEach items="${products}" var="p">
                <div class="product-card">
                    <c:choose>
                        <c:when test="${not empty p.image}">
                            <c:choose>
                                <c:when test="${p.image.startsWith('http')}">
                                    <img class="product-image" src="${p.image}" alt="${p.name}">
                                </c:when>
                                <c:otherwise>
                                    <img class="product-image" src="${pageContext.request.contextPath}/image?fname=${p.image}" alt="${p.name}">
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <img class="product-image" src="https://via.placeholder.com/220x180?text=No+Image" alt="${p.name}">
                        </c:otherwise>
                    </c:choose>

                    <div class="product-info">
                        <a href="${pageContext.request.contextPath}/product/detail?id=${p.id}" class="product-name">${p.name}</a>
                        <div class="product-price">
                            <fmt:formatNumber value="${p.price}" pattern="#,##0" /> VNĐ
                        </div>
                        <div class="product-quantity">Số lượng: ${p.quantity}</div>
                        <div class="product-action">
                            <a href="${pageContext.request.contextPath}/product/detail?id=${p.id}" class="btn-detail">Xem chi tiết</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="no-products">Hiện chưa có sản phẩm nào.</div>
        </c:otherwise>
    </c:choose>
</div>

