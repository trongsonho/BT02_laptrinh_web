package vn.iotstar.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/home-session" })
public class HomeSessionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        resp.setContentType(
                "text/html;charset=UTF-8"
        );

        HttpSession session =
                req.getSession(false);

        PrintWriter out =
                resp.getWriter();

        out.println("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Trang chủ</title>
                </head>

                <body>
                """);

        if (session != null) {

            String username =
                    (String) session.getAttribute(
                            "username"
                    );

            if (username != null) {

                out.println(
                        "<h2>Chào mừng "
                        + username
                        + "!</h2>"
                );

                out.println(
                        "<p>Session ID: "
                        + session.getId()
                        + "</p>"
                );

                out.println("<hr>");

                out.println("""
                        <h3>Chức năng</h3>

                        <p>
                            <a href="%s/admin/category/list">
                                Quản lý Category
                            </a>
                        </p>

                        <p>
                            <a href="%s/logout-session">
                                Đăng xuất
                            </a>
                        </p>
                        """.formatted(
                                req.getContextPath(),
                                req.getContextPath()
                        ));

            } else {

                showLoginLink(out, req);
            }

        } else {

            showLoginLink(out, req);
        }

        out.println("""
                </body>
                </html>
                """);
    }

    private void showLoginLink(
            PrintWriter out,
            HttpServletRequest req) {

        out.println("""
                <h2>Bạn chưa đăng nhập!</h2>

                <a href="%s/login-session.html">
                    Đăng nhập
                </a>
                """.formatted(
                        req.getContextPath()
                ));
    }
}