<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<title>Sửa danh mục - MyShop Admin</title>

<div class="row justify-content-center my-4">
    <div class="col-md-6 col-lg-5">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-success text-white py-3">
                <h4 class="mb-0 fw-bold"><i class="bi bi-pencil-square me-2"></i>Sửa danh mục #${category.id}</h4>
            </div>
            <div class="card-body p-4">
                <form action="edit" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="${category.id}"/>

                    <div class="mb-3">
                        <label for="name" class="form-label fw-semibold">Tên danh mục (*):</label>
                        <input type="text" class="form-control" id="name" name="name" value="${category.name}" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Ảnh đại diện hiện tại:</label>
                        <c:choose>
                            <c:when test="${not empty category.icon}">
                                <div>
                                    <img src="${pageContext.request.contextPath}/image?fname=${category.icon}" 
                                         width="80" height="80" style="object-fit:cover; border-radius:4px; border:1px solid #dee2e6;" alt="${category.name}"/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-light text-muted border">Chưa có ảnh</span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="mb-4">
                        <label for="icon" class="form-label fw-semibold">Chọn ảnh mới (để trống nếu không đổi):</label>
                        <input type="file" class="form-control" id="icon" name="icon" accept="image/*">
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-success px-4 fw-bold">
                            <i class="bi bi-check-circle me-1"></i>Cập nhật danh mục
                        </button>
                        <a href="list" class="btn btn-secondary px-4">
                            Quay lại danh sách
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>