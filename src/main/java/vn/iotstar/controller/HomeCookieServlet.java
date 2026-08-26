package vn.iotstar.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/home-cookie" })
public class HomeCookieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType(
                "text/html;charset=UTF-8"
        );

        PrintWriter out =
                resp.getWriter();

        String username = null;

        Cookie[] cookies =
                req.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                if ("username".equals(
                        cookie.getName())) {

                    username =
                            cookie.getValue();

                    break;
                }
            }
        }

        out.println("""
                <!DOCTYPE html>
                <html>

                <head>
                    <meta charset="UTF-8">
                    <title>Trang chủ</title>
                </head>

                <body>
                """);

        if (username != null
                && !username.isEmpty()) {

            out.println(
                    "<h2>Chào mừng "
                    + username
                    + "!</h2>"
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
                        <a href="%s/logout-cookie">
                            Đăng xuất
                        </a>
                    </p>
                    """.formatted(
                            req.getContextPath(),
                            req.getContextPath()
                    ));

        } else {

            out.println("""
                    <h2>Bạn chưa đăng nhập!</h2>

                    <a href="%s/login.html">
                        Đăng nhập
                    </a>
                    """.formatted(
                            req.getContextPath()
                    ));
        }

        out.println("""
                </body>
                </html>
                """);
    }
}