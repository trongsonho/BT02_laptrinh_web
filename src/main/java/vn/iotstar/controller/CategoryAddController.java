package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = { "/admin/category/add" })
public class CategoryAddController extends HttpServlet {

    private static final long serialVersionUID = 1L;

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

        RequestDispatcher dispatcher =
                req.getRequestDispatcher(
                        "/views/admin/add-category.jsp"
                );

        dispatcher.include(req, resp);
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        if (!vn.iotstar.util.AuthUtil.checkAdmin(req, resp)) {
            return;
        }

        String categoryName = null;
        DiskFileItem iconItem = null;

        DiskFileItemFactory factory =
                DiskFileItemFactory.builder().get();

        JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory>
                upload =
                new JakartaServletFileUpload<>(factory);

        try {
            List<DiskFileItem> items =
                    upload.parseRequest(req);

            for (DiskFileItem item : items) {
                if (item.isFormField()) {
                    if ("name".equals(item.getFieldName())) {
                        categoryName = item.getString(StandardCharsets.UTF_8);
                    }
                } else {
                    if ("icon".equals(item.getFieldName()) && item.getSize() > 0) {
                        iconItem = item;
                    }
                }
            }

            String trimmedName = (categoryName != null) ? categoryName.trim() : "";
            req.setAttribute("name", trimmedName);

            String error = null;

            // 1. Validate name
            if (trimmedName.isEmpty()) {
                error = "Tên danh mục không được để trống.";
            } else if (trimmedName.length() < 2 || trimmedName.length() > 100) {
                error = "Tên danh mục phải có từ 2 đến 100 ký tự.";
            } else if (vn.iotstar.util.ValidationUtil.hasControlCharacters(trimmedName)) {
                error = "Tên danh mục chứa ký tự không hợp lệ.";
            } else {
                Category existing = categoryService.get(trimmedName);
                if (existing != null) {
                    error = "Tên danh mục đã tồn tại.";
                }
            }

            // 2. Validate icon file
            String fileName = null;
            if (error == null && iconItem != null) {
                String originalFileName = iconItem.getName();
                if (!vn.iotstar.util.ValidationUtil.isValidImageExtension(originalFileName)) {
                    error = "Hình ảnh không hợp lệ. Chỉ chấp nhận các định dạng .jpg, .jpeg, .png, .gif, .webp.";
                } else if (!vn.iotstar.util.ValidationUtil.isValidImageMime(iconItem.getContentType())) {
                    error = "File tải lên không phải là định dạng hình ảnh hợp lệ.";
                } else if (iconItem.getSize() > vn.iotstar.util.ValidationUtil.MAX_IMAGE_SIZE_BYTES) {
                    error = "Dung lượng ảnh vượt quá giới hạn cho phép (tối đa 5MB).";
                } else {
                    String ext = vn.iotstar.util.ValidationUtil.getFileExtension(originalFileName);
                    fileName = System.currentTimeMillis() + "-" + java.util.UUID.randomUUID().toString().substring(0, 8) + ext;
                }
            }

            // Nếu có lỗi validation
            if (error != null) {
                req.setAttribute("error", error);
                RequestDispatcher dispatcher =
                        req.getRequestDispatcher("/views/admin/add-category.jsp");
                dispatcher.include(req, resp);
                return;
            }

            // Lưu file và cập nhật database sau khi đã validate hợp lệ
            Category category = new Category();
            if (fileName != null && iconItem != null) {
                File dir = new File(Constant.DIR + "/category");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, fileName);
                iconItem.write(file.toPath());
                category.setIcon("category/" + fileName);
            }

            category.setName(trimmedName);
            categoryService.insert(category);

            resp.sendRedirect(
                    req.getContextPath()
                    + "/admin/category/list"
            );

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi xử lý thêm danh mục: " + e.getMessage());
            RequestDispatcher dispatcher =
                    req.getRequestDispatcher("/views/admin/add-category.jsp");
            dispatcher.include(req, resp);
        }
    }
}