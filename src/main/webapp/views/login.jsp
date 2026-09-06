<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Đăng nhập</title>

</head>

<body>

<div style="
    width:400px;
    margin:80px auto;
    padding:30px;
    border:1px solid #ddd;
">

    <h2>Đăng nhập</h2>

    <%
        String error =
            (String) request.getAttribute("error");

        String message =
            (String) session.getAttribute("message");

        if (error != null) {
    %>

        <p style="color:red">
            <%= error %>
        </p>

    <%
        }

        if (message != null) {
    %>

        <p style="color:green">
            <%= message %>
        </p>

    <%
            session.removeAttribute("message");
        }
    %>

    <form action="<%= request.getContextPath() %>/login"
          method="post">

        <label>Tên đăng nhập</label>

        <input
            type="text"
            name="username"
            required
        >

        <br><br>

        <label>Mật khẩu</label>

        <input
            type="password"
            name="password"
            required
        >

        <br><br>

        <button type="submit">
            Đăng nhập
        </button>

    </form>

    <p>
        <a href="<%= request.getContextPath() %>/register">
            Đăng ký
        </a>
    </p>

    <p>
        <a href="<%= request.getContextPath() %>/forgot-password">
            Quên mật khẩu?
        </a>
    </p>

</div>

</body>
</html>