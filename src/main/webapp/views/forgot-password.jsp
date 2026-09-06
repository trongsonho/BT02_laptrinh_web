<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Quên mật khẩu</title>

</head>

<body>

<div style="
    width:400px;
    margin:80px auto;
    padding:30px;
    border:1px solid #ddd;
">

    <h2>Quên mật khẩu</h2>

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

    <form action="<%= request.getContextPath() %>/forgot-password"
          method="post">

        <label>Email</label>

        <input
            type="email"
            name="email"
            required
        >

        <br><br>

        <button type="submit">
            Gửi OTP
        </button>

    </form>

</div>

</body>
</html>