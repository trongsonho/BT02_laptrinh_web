<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Đặt lại mật khẩu</title>

</head>

<body>

<div style="
    width:400px;
    margin:80px auto;
    padding:30px;
    border:1px solid #ddd;
">

    <h2>Đặt lại mật khẩu</h2>

    <%
        String error =
            (String) request.getAttribute("error");

        if (error != null) {
    %>

        <p style="color:red">
            <%= error %>
        </p>

    <%
        }
    %>

    <form action="<%= request.getContextPath() %>/reset-password"
          method="post">

        <label>OTP</label>

        <input
            type="text"
            name="otp"
            maxlength="6"
            required
        >

        <br><br>

        <label>Mật khẩu mới</label>

        <input
            type="password"
            name="password"
            required
        >

        <br><br>

        <label>Xác nhận mật khẩu</label>

        <input
            type="password"
            name="confirmPassword"
            required
        >

        <br><br>

        <button type="submit">
            Đặt lại mật khẩu
        </button>

    </form>

</div>

</body>
</html>