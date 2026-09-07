<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>Đăng ký</title>

    <style>

        body {
            font-family: Arial;
            background: #f5f5f5;
        }

        .container {
            width: 400px;
            margin: 70px auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 3px 15px #ccc;
        }

        input {
            width: 100%;
            padding: 10px;
            margin: 8px 0 15px;
            box-sizing: border-box;
        }

        button {
            width: 100%;
            padding: 11px;
            background: #007bff;
            color: white;
            border: none;
            cursor: pointer;
        }

        .error {
            color: red;
        }

    </style>

</head>

<body>

<div class="container">

    <h2>Đăng ký tài khoản</h2>

    <%
        String error =
            (String) request.getAttribute("error");

        if (error != null) {
    %>

        <p class="error">
            <%= error %>
        </p>

    <%
        }
    %>

    <form action="<%= request.getContextPath() %>/register"
          method="post">

        <label>Tên đăng nhập</label>
        <input type="text"
               name="username"
               id="username"
               value=""
               autocomplete="off"
               required>

        <label>Email</label>
        <input type="email"
               name="email"
               id="email"
               value=""
               autocomplete="email"
               required>

        <label>Mật khẩu</label>
        <input type="password"
               name="password"
               id="password"
               value=""
               autocomplete="new-password"
               required>

        <label>Xác nhận mật khẩu</label>
        <input type="password"
               name="confirmPassword"
               id="confirmPassword"
               value=""
               autocomplete="new-password"
               required>

        <button type="submit">
            Đăng ký
        </button>

    </form>

    <p>
        Đã có tài khoản?
        <a href="<%= request.getContextPath() %>/login">
            Đăng nhập
        </a>
    </p>

</div>

</body>
</html>