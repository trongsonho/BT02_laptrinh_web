<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Xác nhận OTP</title>
</head>

<body>

<div style="
    width:400px;
    margin:80px auto;
    padding:30px;
    border:1px solid #ddd;
">

    <h2>Kích hoạt tài khoản</h2>

    <p>
        Mã OTP đã được gửi tới email của bạn.
    </p>

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

    <form action="<%= request.getContextPath() %>/verify-otp"
          method="post">

        <input
            type="text"
            name="otp"
            placeholder="Nhập mã OTP"
            maxlength="6"
            required
        >

        <br><br>

        <button type="submit">
            Xác nhận
        </button>

    </form>

</div>

</body>
</html>