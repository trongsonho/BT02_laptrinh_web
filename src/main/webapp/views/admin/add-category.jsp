<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<title>Thêm danh mục - MyShop Admin</title>

<div class="row justify-content-center my-4">
    <div class="col-md-6 col-lg-5">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-primary text-white py-3">
                <h4 class="mb-0 fw-bold"><i class="bi bi-plus-circle me-2"></i>Thêm danh mục mới</h4>
            </div>
            <div class="card-body p-4">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger d-flex align-items-center mb-3" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        <div>${error}</div>
                    </div>
                </c:if>

                <form action="add" method="post" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label for="name" class="form-label fw-semibold">Tên danh mục (*):</label>
                        <input type="text" class="form-control" id="name" name="name" 
                               value="${not empty name ? name : ''}" 
                               minlength="2" maxlength="100" required placeholder="Nhập tên danh mục...">
                    </div>

                    <div class="mb-4">
                        <label for="icon" class="form-label fw-semibold">Ảnh đại diện (Icon):</label>
                        <input type="file" class="form-control" id="icon" name="icon" accept=".jpg,.jpeg,.png,.gif,.webp,image/*">
                        <small class="text-muted">Định dạng hỗ trợ: .jpg, .jpeg, .png, .gif, .webp (Tối đa 5MB)</small>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-primary px-4 fw-bold">
                            <i class="bi bi-save me-1"></i>Thêm danh mục
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