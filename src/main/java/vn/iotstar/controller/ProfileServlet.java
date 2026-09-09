package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.entity.User;
import vn.iotstar.util.Constant;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebServlet("/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,        // 1 MB
        maxFileSize = 1024 * 1024 * 5,          // 5 MB
        maxRequestSize = 1024 * 1024 * 20       // 20 MB
)
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp");
    private final UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User sessionUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (sessionUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser = userDao.findById(sessionUser.getId());
        if (currentUser == null) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setAttribute("user", currentUser);
        req.getRequestDispatcher("/profile.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User sessionUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (sessionUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = userDao.findById(sessionUser.getId());
        if (user == null) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");
        String error = null;

        // 1. Validate email
        String normalizedEmail = (email != null) ? email.trim().toLowerCase() : "";
        if (normalizedEmail.isEmpty()) {
            error = "Email không được để trống.";
        } else if (!vn.iotstar.util.ValidationUtil.isValidEmail(normalizedEmail)) {
            error = "Email không hợp lệ.";
        } else {
            User existing = userDao.findByEmail(normalizedEmail);
            if (existing != null && !existing.getId().equals(user.getId())) {
                error = "Email này đã được sử dụng bởi tài khoản khác.";
            }
        }

        // 2. Validate fullname
        if (error == null) {
            if (fullname == null || fullname.trim().isEmpty()) {
                error = "Họ và tên không được để trống.";
            } else if (fullname.trim().length() > 150) {
                error = "Họ và tên không được vượt quá 150 ký tự.";
            } else if (vn.iotstar.util.ValidationUtil.hasControlCharacters(fullname)) {
                error = "Họ và tên chứa ký tự không hợp lệ.";
            }
        }

        // 3. Validate phone
        if (error == null) {
            if (phone == null || phone.trim().isEmpty()) {
                error = "Số điện thoại không được để trống.";
            } else {
                String trimmedPhone = phone.trim();
                // Check format: 10-11 digits starting with 0
                if (!trimmedPhone.matches("^0[0-9]{9,10}$")) {
                    error = "Số điện thoại không hợp lệ (yêu cầu 10-11 chữ số và bắt đầu bằng số 0).";
                }
            }
        }

        // 4. Handle image upload (optional)
        Part imagePart = null;
        try {
            imagePart = req.getPart("image");
        } catch (Exception e) {
            error = "Lỗi khi xử lý file upload: " + e.getMessage();
        }

        String newImageRelPath = null;
        if (error == null && imagePart != null && imagePart.getSize() > 0
                && imagePart.getSubmittedFileName() != null
                && !imagePart.getSubmittedFileName().isBlank()) {

            String submittedFileName = imagePart.getSubmittedFileName();
            String ext = "";
            int lastDot = submittedFileName.lastIndexOf('.');
            if (lastDot >= 0) {
                ext = submittedFileName.substring(lastDot).toLowerCase();
            }

            // Check extension
            if (!ALLOWED_EXTENSIONS.contains(ext)) {
                error = "Ảnh không hợp lệ. Chỉ chấp nhận các định dạng .jpg, .jpeg, .png, .gif, .webp.";
            }

            // Check MIME type
            if (error == null) {
                String contentType = imagePart.getContentType();
                if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
                    error = "File tải lên không phải là định dạng hình ảnh hợp lệ.";
                }
            }

            // Check size (<= 5MB)
            if (error == null && imagePart.getSize() > 5 * 1024 * 1024) {
                error = "Dung lượng ảnh vượt quá giới hạn cho phép (tối đa 5MB).";
            }

            if (error == null) {
                String fileName = UUID.randomUUID().toString() + ext;
                String uploadPath = Constant.DIR + "/users";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                imagePart.write(uploadPath + File.separator + fileName);
                newImageRelPath = "users/" + fileName;

                // Delete old image if it was previously uploaded
                String oldImage = user.getImage();
                if (oldImage != null && oldImage.startsWith("users/")) {
                    try {
                        File oldFile = new File(Constant.DIR + File.separator + oldImage);
                        if (oldFile.exists() && oldFile.isFile()) {
                            oldFile.delete();
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        // If validation failed, retain inputs and show error
        if (error != null) {
            User tempUser = new User();
            tempUser.setId(user.getId());
            tempUser.setUsername(user.getUsername());
            tempUser.setEmail(email != null ? email.trim() : "");
            tempUser.setRole(user.getRole());
            tempUser.setImage(user.getImage()); // preserve current image
            tempUser.setFullname(fullname);
            tempUser.setPhone(phone);

            req.setAttribute("user", tempUser);
            req.setAttribute("error", error);
            req.getRequestDispatcher("/profile.jsp").include(req, resp);
            return;
        }

        // Apply updates
        user.setEmail(normalizedEmail);
        user.setFullname(fullname.trim());
        user.setPhone(phone.trim());
        if (newImageRelPath != null) {
            user.setImage(newImageRelPath);
        }
        // If no new image was uploaded, user.getImage() is preserved unchanged

        // Persist via JPA
        userDao.update(user);

        // Update session
        session.setAttribute("user", user);

        req.setAttribute("user", user);
        req.setAttribute("message", "Cập nhật hồ sơ thành công!");
        req.getRequestDispatcher("/profile.jsp").include(req, resp);
    }
}
