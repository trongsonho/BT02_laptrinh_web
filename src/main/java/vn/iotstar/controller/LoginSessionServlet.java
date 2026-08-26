package vn.iotstar.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/login-session" })
public class LoginSessionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        resp.setContentType("text/html;charset=UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if ("admin".equals(username)
                && "123".equals(password)) {

            // =========================
            // TẠO SESSION
            // =========================
            HttpSession session =
                    req.getSession(true);

            session.setAttribute(
                    "username",
                    username
            );

            session.setMaxInactiveInterval(
                    30 * 60
            );

            // =========================
            // HIỂN THỊ TRANG SAU LOGIN
            // =========================
            PrintWriter out =
                    resp.getWriter();

            out.println("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <title>Đăng nhập thành công</title>
                    </head>

                    <body>

                        <h2>Đăng nhập thành công!</h2>

                        <p>Xin chào: %s</p>

                        <hr>

                        <h3>Chức năng</h3>

                        <p>
                            <a href="%s/admin/category/list">
                                Quản lý Category
                            </a>
                        </p>

                        <p>
                            <a href="%s/home-session">
                                Trang chủ
                            </a>
                        </p>

                        <p>
                            <a href="%s/logout-session">
                                Đăng xuất
                            </a>
                        </p>

                    </body>
                    </html>
                    """.formatted(
                            username,
                            req.getContextPath(),
                            req.getContextPath(),
                            req.getContextPath()
                    ));
        }

        else {

            resp.getWriter().println("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <title>Đăng nhập</title>
                    </head>

                    <body>

                        <h2 style="color:red">
                            Sai tên đăng nhập hoặc mật khẩu!
                        </h2>

                        <a href="login-session.html">
                            Quay lại đăng nhập
                        </a>

                    </body>
                    </html>
                    """);
        }
    }
}