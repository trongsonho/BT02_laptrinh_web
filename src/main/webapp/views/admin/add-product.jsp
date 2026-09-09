<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<title>Thêm sản phẩm mới - MyShop Admin</title>

<div class="row justify-content-center my-4">
    <div class="col-md-8 col-lg-6">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-primary text-white py-3">
                <h4 class="mb-0 fw-bold"><i class="bi bi-plus-circle me-2"></i>Thêm sản phẩm mới</h4>
            </div>
            <div class="card-body p-4">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger d-flex align-items-center mb-3" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        <div>${error}</div>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/admin/products/add" method="post" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label for="name" class="form-label fw-semibold">Tên sản phẩm (*):</label>
                        <input type="text" class="form-control" id="name" name="name" 
                               value="${not empty name ? name : ''}" 
                               minlength="2" maxlength="200" required placeholder="Nhập tên sản phẩm...">
                    </div>

                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label for="price" class="form-label fw-semibold">Giá (VNĐ) (*):</label>
                            <input type="number" class="form-control" id="price" name="price" 
                                   value="${not empty price ? price : ''}" 
                                   step="any" min="0" required placeholder="0">
                        </div>
                        <div class="col-md-6">
                            <label for="quantity" class="form-label fw-semibold">Số lượng (*):</label>
                            <input type="number" class="form-control" id="quantity" name="quantity" 
                                   value="${not empty quantity ? quantity : ''}" 
                                   min="0" step="1" required placeholder="0">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="categoryId" class="form-label fw-semibold">Danh mục:</label>
                        <select class="form-select" id="categoryId" name="categoryId">
                            <option value="">-- Chọn danh mục --</option>
                            <c:forEach items="${categories}" var="cat">
                                <option value="${cat.id}" ${categoryId == cat.id ? 'selected' : ''}>${cat.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="image" class="form-label fw-semibold">Hình ảnh sản phẩm:</label>
                        <input type="file" class="form-control" id="image" name="image" accept=".jpg,.jpeg,.png,.gif,.webp,image/*">
                        <small class="text-muted">Định dạng hỗ trợ: .jpg, .jpeg, .png, .gif, .webp (Tối đa 5MB)</small>
                    </div>

                    <div class="mb-4">
                        <label for="description" class="form-label fw-semibold">Mô tả sản phẩm:</label>
                        <textarea class="form-control" id="description" name="description" rows="4" maxlength="5000" placeholder="Nhập mô tả chi tiết sản phẩm...">${not empty description ? description : ''}</textarea>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-primary px-4 fw-bold">
                            <i class="bi bi-save me-1"></i>Lưu sản phẩm
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

