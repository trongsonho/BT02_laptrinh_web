<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<title>Chỉnh sửa sản phẩm - MyShop Admin</title>

<style>
    .img-preview { width: 100px; height: 100px; object-fit: cover; border-radius: 4px; border: 1px solid #dee2e6; }
</style>

<div class="row justify-content-center my-4">
    <div class="col-md-8 col-lg-6">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-success text-white py-3">
                <h4 class="mb-0 fw-bold"><i class="bi bi-pencil-square me-2"></i>Chỉnh sửa sản phẩm #${product.id}</h4>
            </div>
            <div class="card-body p-4">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger d-flex align-items-center mb-3" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        <div>${error}</div>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/admin/products/edit" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="${product.id}">

                    <div class="mb-3">
                        <label for="name" class="form-label fw-semibold">Tên sản phẩm (*):</label>
                        <input type="text" class="form-control" id="name" name="name" 
                               value="${product.name}" minlength="2" maxlength="200" required>
                    </div>

                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label for="price" class="form-label fw-semibold">Giá (VNĐ) (*):</label>
                            <input type="number" class="form-control" id="price" name="price" 
                                   value="${product.price}" step="any" min="0" required>
                        </div>
                        <div class="col-md-6">
                            <label for="quantity" class="form-label fw-semibold">Số lượng (*):</label>
                            <input type="number" class="form-control" id="quantity" name="quantity" 
                                   value="${product.quantity}" min="0" step="1" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="categoryId" class="form-label fw-semibold">Danh mục:</label>
                        <select class="form-select" id="categoryId" name="categoryId">
                            <option value="">-- Chọn danh mục --</option>
                            <c:forEach items="${categories}" var="cat">
                                <option value="${cat.id}" ${not empty product.category && cat.id == product.category.id ? 'selected' : ''}>${cat.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="image" class="form-label fw-semibold">Hình ảnh sản phẩm (để trống nếu không đổi):</label>
                        <input type="file" class="form-control" id="image" name="image" accept=".jpg,.jpeg,.png,.gif,.webp,image/*">
                        <c:if test="${not empty product.image}">
                            <div class="mt-2">
                                <small class="text-muted d-block mb-1">Ảnh hiện tại:</small>
                                <c:choose>
                                    <c:when test="${product.image.startsWith('http')}">
                                        <img class="img-preview" src="${product.image}" alt="${product.name}">
                                    </c:when>
                                    <c:otherwise>
                                        <img class="img-preview" src="${pageContext.request.contextPath}/image?fname=${product.image}" alt="${product.name}">
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>
                    </div>

                    <div class="mb-4">
                        <label for="description" class="form-label fw-semibold">Mô tả sản phẩm:</label>
                        <textarea class="form-control" id="description" name="description" rows="4" maxlength="5000">${product.description}</textarea>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-success px-4 fw-bold">
                            <i class="bi bi-check-circle me-1"></i>Cập nhật sản phẩm
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-secondary px-4">
                            Hủy bỏ
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

