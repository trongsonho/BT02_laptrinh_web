<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<title>Danh sách danh mục - MyShop Admin</title>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h3 class="fw-bold mb-0 text-dark">
        <i class="bi bi-tags me-2 text-primary"></i>Danh sách danh mục
    </h3>
    <div>
        <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline-secondary me-2">
            <i class="bi bi-box-seam me-1"></i>Quản lý Sản phẩm
        </a>
        <a href="${pageContext.request.contextPath}/admin/category/add" class="btn btn-primary">
            <i class="bi bi-plus-circle me-1"></i>Thêm danh mục
        </a>
    </div>
</div>

<div class="card shadow-sm border-0">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover table-striped align-middle mb-0">
                <thead class="table-dark">
                    <tr>
                        <th class="text-center" style="width: 60px;">STT</th>
                        <th style="width: 100px;">Hình ảnh</th>
                        <th>Tên danh mục</th>
                        <th class="text-center" style="width: 150px;">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty categoryList}">
                            <c:forEach items="${categoryList}" var="cate" varStatus="STT">
                                <tr>
                                    <td class="text-center">${STT.index + 1}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty cate.icon}">
                                                <img src="${pageContext.request.contextPath}/image?fname=${cate.icon}" 
                                                     width="60" height="60" style="object-fit:cover; border-radius:4px;" alt="${cate.name}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-light text-muted border">Không ảnh</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><strong class="text-dark">${cate.name}</strong></td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/admin/category/edit?id=${cate.id}" class="btn btn-sm btn-outline-success me-1" title="Sửa">
                                            <i class="bi bi-pencil"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/category/delete?id=${cate.id}" onclick="return confirm('Bạn có chắc muốn xóa danh mục này?');" class="btn btn-sm btn-outline-danger" title="Xóa">
                                            <i class="bi bi-trash"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="4" class="text-center py-4 text-muted">Chưa có danh mục nào.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>