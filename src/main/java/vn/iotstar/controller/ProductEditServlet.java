package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.Constant;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/admin/products/edit")
@MultipartConfig
public class ProductEditServlet
        extends HttpServlet {

    private final ProductService productService =
            new ProductServiceImpl();
    private final CategoryService categoryService =
            new CategoryServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        if (!vn.iotstar.util.AuthUtil.checkAdmin(req, resp)) {
            return;
        }

        String idParam =
                req.getParameter("id");

        if (idParam == null) {
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST);

            return;
        }

        Product product =
                productService.findById(
                        Long.parseLong(idParam));

        if (product == null) {
            resp.sendError(
                    HttpServletResponse.SC_NOT_FOUND);

            return;
        }

        req.setAttribute(
                "categories",
                categoryService.getAll());

        req.setAttribute(
                "product",
                product);

        req.getRequestDispatcher(
                "/views/admin/edit-product.jsp")
                .include(req, resp);
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        if (!vn.iotstar.util.AuthUtil.checkAdmin(req, resp)) {
            return;
        }

        req.setCharacterEncoding("UTF-8");

        String idStr = req.getParameter("id");
        Long id = null;
        try {
            if (idStr != null) {
                id = Long.parseLong(idStr.trim());
            }
        } catch (Exception e) {
            id = null;
        }

        if (id == null) {
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Mã sản phẩm không hợp lệ.");
            return;
        }

        Product product =
                productService.findById(id);

        if (product == null) {
            resp.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Không tìm thấy sản phẩm.");
            return;
        }

        String name = req.getParameter("name");
        String priceStr = req.getParameter("price");
        String quantityStr = req.getParameter("quantity");
        String categoryIdStr = req.getParameter("categoryId");
        String description = req.getParameter("description");

        String trimmedName = (name != null) ? name.trim() : "";
        String trimmedPrice = (priceStr != null) ? priceStr.trim() : "";
        String trimmedQuantity = (quantityStr != null) ? quantityStr.trim() : "";
        String trimmedCategory = (categoryIdStr != null) ? categoryIdStr.trim() : "";
        String trimmedDescription = (description != null) ? description.trim() : "";

        String error = null;

        // 1. Validate name
        if (trimmedName.isEmpty()) {
            error = "Tên sản phẩm không được để trống.";
        } else if (trimmedName.length() < 2 || trimmedName.length() > 200) {
            error = "Tên sản phẩm phải có từ 2 đến 200 ký tự.";
        } else if (vn.iotstar.util.ValidationUtil.hasControlCharacters(trimmedName)) {
            error = "Tên sản phẩm chứa ký tự không hợp lệ.";
        }

        // 2. Validate price
        BigDecimal price = null;
        if (error == null) {
            if (trimmedPrice.isEmpty()) {
                error = "Giá sản phẩm không được để trống.";
            } else {
                price = vn.iotstar.util.ValidationUtil.parseNonNegativeBigDecimal(trimmedPrice);
                if (price == null) {
                    error = "Giá sản phẩm phải là số không âm hợp lệ.";
                }
            }
        }

        // 3. Validate quantity
        Integer quantity = null;
        if (error == null) {
            if (trimmedQuantity.isEmpty()) {
                error = "Số lượng sản phẩm không được để trống.";
            } else {
                quantity = vn.iotstar.util.ValidationUtil.parseNonNegativeInteger(trimmedQuantity);
                if (quantity == null) {
                    error = "Số lượng phải là số nguyên không âm hợp lệ.";
                }
            }
        }

        // 4. Validate categoryId
        Category category = null;
        if (error == null && !trimmedCategory.isEmpty()) {
            try {
                int catId = Integer.parseInt(trimmedCategory);
                category = categoryService.get(catId);
                if (category == null) {
                    error = "Danh mục đã chọn không tồn tại.";
                }
            } catch (Exception e) {
                error = "Mã danh mục không hợp lệ.";
            }
        }

        // 5. Validate description
        if (error == null && trimmedDescription.length() > 5000) {
            error = "Mô tả sản phẩm không được vượt quá 5000 ký tự.";
        }

        // 6. Validate Image upload (optional)
        Part imagePart = null;
        try {
            imagePart = req.getPart("image");
        } catch (Exception e) {
            error = "Lỗi khi xử lý file tải lên: " + e.getMessage();
        }

        String newFileName = null;
        if (error == null && imagePart != null && imagePart.getSize() > 0
                && imagePart.getSubmittedFileName() != null
                && !imagePart.getSubmittedFileName().isBlank()) {

            String submittedName = imagePart.getSubmittedFileName();
            if (!vn.iotstar.util.ValidationUtil.isValidImageExtension(submittedName)) {
                error = "Hình ảnh không hợp lệ. Chỉ chấp nhận các định dạng .jpg, .jpeg, .png, .gif, .webp.";
            } else if (!vn.iotstar.util.ValidationUtil.isValidImageMime(imagePart.getContentType())) {
                error = "File tải lên không phải là định dạng hình ảnh hợp lệ.";
            } else if (imagePart.getSize() > vn.iotstar.util.ValidationUtil.MAX_IMAGE_SIZE_BYTES) {
                error = "Dung lượng ảnh vượt quá giới hạn cho phép (tối đa 5MB).";
            } else {
                String ext = vn.iotstar.util.ValidationUtil.getFileExtension(submittedName);
                newFileName = System.currentTimeMillis() + "-" + java.util.UUID.randomUUID().toString().substring(0, 8) + ext;
            }
        }

        // Nếu có lỗi validation, render lại form edit với dữ liệu đã nhập
        if (error != null) {
            Product tempProduct = new Product();
            tempProduct.setId(id);
            tempProduct.setName(trimmedName);
            tempProduct.setPrice(price != null ? price : BigDecimal.ZERO);
            tempProduct.setQuantity(quantity != null ? quantity : 0);
            tempProduct.setDescription(trimmedDescription);
            tempProduct.setImage(product.getImage());
            if (category != null) {
                tempProduct.setCategory(category);
            } else if (!trimmedCategory.isEmpty()) {
                Category c = new Category();
                try { c.setId(Integer.parseInt(trimmedCategory)); } catch (Exception ignored) {}
                tempProduct.setCategory(c);
            }

            req.setAttribute("error", error);
            req.setAttribute("product", tempProduct);
            req.setAttribute("categories", categoryService.getAll());
            req.getRequestDispatcher(
                    "/views/admin/edit-product.jsp")
                    .include(req, resp);
            return;
        }

        if (newFileName != null) {
            String uploadPath =
                    Constant.DIR
                            + "/product";

            java.io.File directory =
                    new java.io.File(uploadPath);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            imagePart.write(
                    uploadPath
                            + "/"
                            + newFileName);

            product.setImage(
                    "product/"
                            + newFileName);
        }

        product.setName(trimmedName);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setDescription(trimmedDescription);
        product.setCategory(category);

        productService.update(product);

        resp.sendRedirect(
                req.getContextPath()
                        + "/admin/products");
    }
}
