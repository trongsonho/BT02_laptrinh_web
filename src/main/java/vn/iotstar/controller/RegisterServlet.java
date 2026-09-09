package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.service.AuthService;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet
        extends HttpServlet {

    private final AuthService authService =
            new AuthService();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        req.getRequestDispatcher("/register.jsp").include(req, resp);
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        String trimmedUsername = (username != null) ? username.trim() : "";
        String trimmedEmail = (email != null) ? email.trim() : "";

        String error;
        try {
            error = authService.register(
                    trimmedUsername,
                    trimmedEmail,
                    password);
        } catch (Exception e) {
            error = "Lỗi xử lý đăng ký: " + e.getMessage();
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("username", trimmedUsername);
            req.setAttribute("email", trimmedEmail);

            req.getRequestDispatcher("/register.jsp").include(req, resp);
            return;
        }

        HttpSession session =
                req.getSession();

        session.setAttribute(
                "verifyEmail",
                email);

        resp.sendRedirect(
                req.getContextPath()
                        + "/verify");
    }
}
