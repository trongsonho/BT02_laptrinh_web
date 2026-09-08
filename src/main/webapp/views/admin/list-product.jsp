<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<title>Quản lý sản phẩm - MyShop Admin</title>

<style>
    .img-thumb { width: 55px; height: 55px; object-fit: cover; border-radius: 4px; }
</style>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h3 class="fw-bold mb-0 text-dark">
        <i class="bi bi-box-seam me-2 text-primary"></i>Danh sách sản phẩm
    </h3>
    <div>
        <a href="${pageContext.request.contextPath}/admin/category/list" class="btn btn-outline-secondary me-2">
            <i class="bi bi-tags me-1"></i>Quản lý Category
        </a>
        <a href="${pageContext.request.contextPath}/admin/products/add" class="btn btn-primary">
            <i class="bi bi-plus-circle me-1"></i>Thêm sản phẩm
        </a>
    </div>
</div>

<div class="card shadow-sm border-0">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover table-striped align-middle mb-0">
                <thead class="table-dark">
                    <tr>
                        <th class="text-center" style="width: 50px;">STT</th>
                        <th style="width: 60px;">ID</th>
                        <th style="width: 80px;">Hình ảnh</th>
                        <th>Tên sản phẩm</th>
                        <th>Giá</th>
                        <th class="text-center">Số lượng</th>
                        <th>Danh mục</th>
                        <th>Ngày tạo</th>
                        <th class="text-center" style="width: 140px;">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty products}">
                            <c:forEach items="${products}" var="p" varStatus="loop">
                                <tr>
                                    <td class="text-center">${loop.index + 1}</td>
                                    <td>#${p.id}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty p.image}">
                                                <c:choose>
                                                    <c:when test="${p.image.startsWith('http')}">
                                                        <img class="img-thumb" src="${p.image}" alt="${p.name}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img class="img-thumb" src="${pageContext.request.contextPath}/image?fname=${p.image}" alt="${p.name}">
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-light text-muted border">Không ảnh</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><strong class="text-dark">${p.name}</strong></td>
                                    <td class="text-danger fw-bold"><fmt:formatNumber value="${p.price}" pattern="#,##0" /> VNĐ</td>
                                    <td class="text-center"><span class="badge bg-secondary">${p.quantity}</span></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty p.category}">
                                                <span class="badge bg-info text-dark">${p.category.name}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <em class="text-muted">Chưa phân loại</em>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="small text-muted">${p.createdAt}</td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/admin/products/edit?id=${p.id}" class="btn btn-sm btn-outline-success me-1" title="Sửa">
                                            <i class="bi bi-pencil"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/products/delete?id=${p.id}" onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?');" class="btn btn-sm btn-outline-danger" title="Xóa">
                                            <i class="bi bi-trash"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="9" class="text-center py-4 text-muted">Không có sản phẩm nào.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

