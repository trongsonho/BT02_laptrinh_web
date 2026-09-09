package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.service.AuthService;

import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet
        extends HttpServlet {

    private final AuthService authService =
            new AuthService();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        if (email == null || email.isBlank()) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                email = (String) session.getAttribute("resetEmail");
            }
        }

        if (email != null) {
            req.setAttribute("email", email);
        }

        req.getRequestDispatcher(
                "/reset-password.jsp")
                .include(req, resp);
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String email =
                req.getParameter("email");

        if (email == null || email.isBlank()) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                email = (String) session.getAttribute("resetEmail");
            }
        }

        String otp =
                req.getParameter("otp");

        String newPassword =
                req.getParameter("password");

        String trimmedEmail = (email != null) ? email.trim() : "";
        req.setAttribute("email", trimmedEmail);

        if (trimmedEmail.isEmpty()) {
            req.setAttribute(
                    "error",
                    "Vui lòng nhập email.");
            req.getRequestDispatcher(
                    "/reset-password.jsp")
                    .include(req, resp);
            return;
        }

        if (!vn.iotstar.util.ValidationUtil.isValidEmail(trimmedEmail)) {
            req.setAttribute(
                    "error",
                    "Email không đúng định dạng.");
            req.getRequestDispatcher(
                    "/reset-password.jsp")
                    .include(req, resp);
            return;
        }

        String trimmedOtp = (otp != null) ? otp.trim() : "";
        req.setAttribute("otp", trimmedOtp);

        if (trimmedOtp.isEmpty()) {
            req.setAttribute(
                    "error",
                    "Vui lòng nhập mã OTP.");
            req.getRequestDispatcher(
                    "/reset-password.jsp")
                    .include(req, resp);
            return;
        }

        if (!vn.iotstar.util.ValidationUtil.isValidOtp(trimmedOtp)) {
            req.setAttribute(
                    "error",
                    "Mã OTP phải gồm đúng 6 chữ số.");
            req.getRequestDispatcher(
                    "/reset-password.jsp")
                    .include(req, resp);
            return;
        }

        if (newPassword == null || newPassword.isEmpty() || newPassword.trim().isEmpty()) {
            req.setAttribute(
                    "error",
                    "Vui lòng nhập mật khẩu mới.");
            req.getRequestDispatcher(
                    "/reset-password.jsp")
                    .include(req, resp);
            return;
        }

        if (!vn.iotstar.util.ValidationUtil.isValidPassword(newPassword)) {
            req.setAttribute(
                    "error",
                    "Mật khẩu mới phải có từ 8 đến 100 ký tự.");
            req.getRequestDispatcher(
                    "/reset-password.jsp")
                    .include(req, resp);
            return;
        }

        boolean success =
                authService.resetPassword(
                        trimmedEmail,
                        trimmedOtp,
                        newPassword);

        if (!success) {

            req.setAttribute(
                    "error",
                    "OTP không hợp lệ hoặc đã hết hạn");

            req.setAttribute(
                    "email",
                    email);

            req.getRequestDispatcher(
                    "/reset-password.jsp")
                    .include(req, resp);

            return;
        }

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute("resetEmail");
        }

        resp.sendRedirect(
                req.getContextPath()
                        + "/login");
    }
}
